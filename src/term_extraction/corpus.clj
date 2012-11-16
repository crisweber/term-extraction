(ns term-extraction.corpus
  (:use [clojure.java.io :only [file]]
        [clojure.string :only [join]]
        [term-extraction.phrases :only [load-phrases-list only-phrases clean-punct-inside-annotated-phrases]]))

  (defn slurp-encoded
    [f]
    (slurp f :encoding "ISO-8859-1"))

  (defn noun-phrases-files
    "Recupera do diretório de corpus os arquivos com os sintagmas nominais extraídos"
    [dir]
    (filter #(.startsWith (.getName %) "OGMA_TRA_" )
      (file-seq dir)))

  (defn define-filename-with-replacement
    [f match replace]
    (let [filename  (.getName f)
          directory (.getAbsolutePath (.getParentFile f))]
      (file directory (.replace filename match replace))))

  (defn destination-file
    "Instância File para arquivo de sintagmas nominais a partir do nome do arquivo de sintagmas nominais extraídos."
    [noun-phrases-file]
    (define-filename-with-replacement noun-phrases-file "OGMA_TRA_" "NOUN_PHRASES_"))

  (defn tagged-phrases-filename
    [noun-phrases-file]
    (define-filename-with-replacement noun-phrases-file "OGMA_TRA_" "TAGGED_NOUN_PHRASES_"))

  (defn create-file-with-phrases-only
    "Cria arquivo com somente os sintagmas nominais do arquivo original de sintagmas nominais extraídos"
    [noun-phrases-file]
    (let [dest-file      (destination-file noun-phrases-file)
          source-content (slurp-encoded noun-phrases-file)
          phrases-list   (load-phrases-list source-content #"/")
          phrases-only   (only-phrases phrases-list)
          dest-content   (join ".\r\n" (doall phrases-only))]
      (do
        (spit dest-file dest-content :encoding "ISO-8859-1"))))

  (defn create-phrase-file-from-all-files
    "Cria arquivo com somente os sintagmas nominais para todos os arquivos originais a partir de um diretório"
    [directory]
    (doseq [noun-phrases-file   (noun-phrases-files directory)]
      (create-file-with-phrases-only noun-phrases-file)))

  (defn load-phrases-for-cleansing
    "Lê os dois arquivos de sintagmas (com ocorrências e anotado) e cria lista única com sintagmas anotados e quantificados.
     Ao formato retornado por esta função devem ser aplicadas as heurísticas."
    [noun-phrases-file]
    (let [tagged-phrases-file (tagged-phrases-filename noun-phrases-file)
          noun-phrases        (load-phrases-list (slurp-encoded noun-phrases-file) #"/")
          tagged-noun-phrases (clean-punct-inside-annotated-phrases (slurp-encoded tagged-phrases-file))]
      (for [[[_ amount _] tagged-phrase]   (map list noun-phrases tagged-noun-phrases)]
        [tagged-phrase amount])))
