(ns term-extraction.terms
  (:use [term-extraction.adjusts :only [adjust bigrams-and-trigrams terms-and-frequencies]]
        [term-extraction.corpus  :only [load-phrases-for-cleansing noun-phrases-files]]
        [clojure.java.io         :only [file]]
        [clojure.string          :only [join]]))

(def corpus-dir (file "/users/cristoferweber/Documents/mestrado/PLN/Corpus_2"))

(defn tf
  [[term grp]]
  (let [freq (->> grp (map second) (reduce +))]
    [term freq]))

(defn terms-from-corpus
  [dir]
  (partition 2
    (flatten
      (for [f (noun-phrases-files dir)]
        (-> (adjust (load-phrases-for-cleansing f))
                    bigrams-and-trigrams
                    terms-and-frequencies )))))

(defn sum-frequency
  [coll]
  (->> coll
       (group-by first)
       (map tf)
       (sort-by second)
       reverse))

(defn process
  [dir]
  (sum-frequency (terms-from-corpus dir)))

(defn as-text
  [coll]
  (join "\r\n" (map #(join \, %) coll)))

(defn to-file [f content] (spit f content :encoding "ISO-8859-1"))

#_(use :reload-all 'term-extraction.terms)
#_(to-file "/Users/cristoferweber/Documents/mestrado/PLN/Corpus_2/result/result-ogma.csv" (->> (process corpus-dir) as-text))