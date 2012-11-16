(ns term-extraction.annotated-test
  (:use clojure.test term-extraction.annotated))

(def sample-text
"    estando/VB os/ADPR receptores/SU cardíacos/AJ ß1/ bloqueados/AJ por/PR e/CJ atenolol/SU ,/VG  pode/VB a/AD ele/PP inferir/VB que/CJPL o/AD fenoterol/SU e/CJ o/AD salbutamol/SU têm/VB atividade/SU semelhante/AJ sobre/PR os/ADPR receptores/SU adrenérgicos/SU cardíacos/AJ ./PN
    outros/AJ efeitos/SU cardíacos/AJ foram/VB observados/AJ nestes/SU pacientes/SU ,/VG  objeto/VBSU de/PR publicação/SU em/PR breve/AVSU ./PN
    de/PR fato/SU ,/VG  a/AD asma/SU aguda/SU grave/VBAJ é/VB uma/AI situação/SU clínica/SU em/PR que/CJPL vários/SU fatores/VBSU ,/VG  tanto/PI decorrentes/SU de/PR a/AD própria/AJ doença/SU como/VBAV de/PR a/AD terapêutica/AJ empregada/AJ ,/VG  se/PP associam/VB ,/VG  e/CJ juntos/AJ ,/VG  constituem/VB um/AINCPI substrato/SU para/PR eventos/SU cardíacos/AJ ameaçadores/SU de/PR a/AD vida/SU ,/VG  servindo/VB como/VBAV alerta/VBSU a/AD os/ADPR pediatras/SU ./PN")

(deftest pair-of-strings-must-be-converted-into-pair-string-keyword
  (testing "Um par formado por duas strings deve ser convertido em um par composto por uma string e uma keyword"
    (let [[w k] (turn-tags-into-keyword ["a" "AT"])]
      (is
        (= ["a" '(:term-extraction.tags/AT)] [w k])))
    ))

(deftest sample-text-contains-three-lines
  (testing "O texto de exemplo deste testcase deve conter somente três linhas"
    (is (= 3 (count (load-annotated-text sample-text ))))
    ))

(deftest second-line-keywords
  (testing "A segunda linha do exemplo deve conter as seguintes tags."
    (is
      (= [[:term-extraction.tags/AJ] [:term-extraction.tags/SU] [:term-extraction.tags/AJ]
          [:term-extraction.tags/VB] [:term-extraction.tags/AJ] [:term-extraction.tags/SU]
          [:term-extraction.tags/SU] [:term-extraction.tags/VG]
          [:term-extraction.tags/VB :term-extraction.tags/SU]
          [:term-extraction.tags/PR] [:term-extraction.tags/SU]
          [:term-extraction.tags/PR] [:term-extraction.tags/AV :term-extraction.tags/SU]
          [:term-extraction.tags/PN]]
         (for [[_ tag] (second (load-annotated-text sample-text))]
           tag)))))
