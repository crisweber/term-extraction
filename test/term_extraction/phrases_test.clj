(ns term-extraction.phrases-test
  (:use clojure.test term-extraction.phrases))

(def sample-text
"a densidade dos receptores/2/0,016000
presentes em os diversos órgãos/2/0,016000
a resposta/3/0,024000
às diversas/2/0,016000
substâncias/2/0,016000
os pulmões humanos/2/0,016000
a proporção de receptores/2/0,016000
90% dos receptores/2/0,016000
beta/3/0,024000
as paredes/2/0,016000
alveolares/2/0,016000
o coração/2/0,016000
a proporção entre receptores/2/0,016000
os átrios e ventrículos/2/0,016000
o desenvolvimento dos broncodilatadores seletivos/2/0,016000
"  )

(deftest sample-text-contains-fifteen-lines
  (testing "O texto de exemplo deste testcase deve conter quinze linhas"
    (is (= 15 (count (load-phrases-list sample-text #"/"))))
    ))

(deftest first-line-of-parsed-sample-text-contains-two-elements
  (testing "A primeira linha extraída do arquivo de exemplo contém os seguintes elementos"
    (is (= ["a densidade dos receptores" 2] (first (load-phrases-list sample-text #"/"))))
    )
  )

(deftest only-phrases-from-list
  (testing "Deve retornar lista com os sintagmas somente"
    (is (= "a densidade dos receptores" (first (only-phrases (load-phrases-list sample-text #"/")))))
    )
  )