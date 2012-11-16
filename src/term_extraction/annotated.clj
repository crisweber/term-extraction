(ns term-extraction.annotated
  (:use [clojure.string :only [split split-lines trim]]))

(defn turn-tags-into-keyword [[word tags]]
  (let [pos-keyword    (comp #(keyword "term-extraction.tags" %) #(apply str %))]
    [word
      (->> tags
        (partition 2)
        (map pos-keyword))]))

(defn load-annotated-text
  "Retorna as sentenças de um texto anotado em listas, uma por sentença,
     cada uma contendo pares [palavra :anotação]"
  [text-content]
    (for [line (split-lines text-content)
          :let [blank #"\s+"
                slash #"/"]]
      (let [annotated-words (split (trim line) blank)]
        (map #(turn-tags-into-keyword (split % slash)) annotated-words))))


