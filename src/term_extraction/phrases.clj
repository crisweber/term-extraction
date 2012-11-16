(ns term-extraction.phrases
  (:use [clojure.string :only [split split-lines trim]]
        [term-extraction.annotated :only [load-annotated-text]]))

(defn- drop-punct
  [coll]
  (filter #(not (some #{:term-extraction.tags/PN} (second %))) coll))

(defn load-phrases-list
  "Carrega lista dos sintagmas nominais com sua contagem de aparições"
  [text-content separator]
  (for [line   (split-lines text-content)]
    (let [[noun-phrase, amount, freq & others]   (map trim (split (trim line) separator))]
      [noun-phrase (Integer/parseInt amount)])))

(defn only-phrases
  "Seleciona somente os sintagmas da lista"
  [phrases-list]
  (map first phrases-list))

(defn clean-punct-inside-annotated-phrases
  "Remove a pontuação inserida artificialmente pela função create-file-with-phrases-only"
  [source-content]
  (let [tagged-phrases   (load-annotated-text source-content)]
    (map drop-punct tagged-phrases)))
