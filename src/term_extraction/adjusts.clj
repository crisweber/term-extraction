(ns term-extraction.adjusts
  (:use [term-extraction.tags :only [grammar]]
        [clojure.string :only [split join]]))

(defn- is-a?
  [class [_ pos-tags]]
    (some #(isa? grammar % class) pos-tags))

(defn- words-in [phrase]
  (split (first phrase) #"\s+"))

(def article :term-extraction.tags/AR)
(def pronoun :term-extraction.tags/PRNM)
(def number  :term-extraction.tags/NU)
(def adverb  :term-extraction.tags/AV)

(defn- drop-head
  [class [coll weight]]
    [(drop-while #(is-a? class) coll) weight])

(defn- drop-all
  [class [coll weight]]
    [(remove (partial is-a? class) coll) weight])

(defn matches-regex?
  [re [terms _]]
  (let [words      (map first terms)]
    (every? #(re-matches re %) words)))

(defn contains-pos?
  [class [terms _]]
  (let [pos-tag  second
        all-pos  (flatten (map pos-tag terms))]
    (some #(isa? grammar % class) all-pos)))

(defn- between [low-limit high-limit value]
  (and (>= value low-limit) (<= value high-limit)))

(defn drop-heading-articles
  "Regra de Ajuste 1 - Remoção de Artigos no Início de SNs"
  [coll]
  (map #(drop-head article %) coll))

(defn drop-all-articles
  "Regra de Ajuste 2 - Remoção de Todos Artigos de SNs"
  [coll]
  (map #(drop-all article %) coll))

(defn drop-heading-pronoun
  "Regra de Ajuste 3 - Remoção de Pronomes no Início de SNs (sem identificação do núcleo)"
  [coll]
  (map #(drop-head pronoun %) coll))

(defn drop-all-pronoun
  "Regra de Ajuste 4 - Remoção de Todos Pronomes de SNs (sem identificação do núcleo)"
  [coll]
  (map #(drop-all pronoun %) coll))

(defn discard-numbers
  "Regra de Descarte 1 - Recusa de SNs com Numerais (não diferencia artigo indefinido 'um' de numeral)"
  [coll]
  (remove #(contains-pos? number %) coll))

(defn discard-symbols
  "Regra de Descarte 2 - Recusa de SNs com Símbolos (mantém dígitos)"
  [coll]
  (filter #(matches-regex? #"[\p{L}|\p{Digit}|-]+" %) coll))

(defn discard-adverbs
  "Regra de Descarte 4 - Recusa de SNs que Iniciam com Advérbio"
  [coll]
  (letfn [(first-tag
            [[[[word [pos & other-pos]] & other-words] amount]]
            pos)]
    (remove #(= adverb (first-tag %)) coll)))

(defn adjust
  "Aplica as Regras em uma lista de Sintagmas"
  [coll]
  (-> coll
    drop-all-articles
    drop-all-pronoun
    discard-numbers
    discard-symbols
    discard-adverbs))

(defn terms-in-noun-phrase
  [[terms _]]
  (let [hiphem    #"-"
        non-empty (complement empty?)]
    (+ (count terms)
       (count
         (filter non-empty
          (for [[word pos] terms]
            (re-seq hiphem word)))))))

(defn bigrams-and-trigrams
  [coll]
    (filter #(between 2 3 (terms-in-noun-phrase %)) coll))

(defn terms-and-frequencies
  [coll]
  (letfn [(content [[term-and-pos frequency]]
            [(join "_" (map first term-and-pos)) frequency])]
    (map content coll)))
