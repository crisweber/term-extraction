(ns term-extraction.tags)

(def grammar
  (-> (make-hierarchy)
    (derive :term-extraction.tags/AD   :term-extraction.tags/AR)    ;Artigo Definido
    (derive :term-extraction.tags/AI   :term-extraction.tags/AR)    ;Artigo Indefinido

    (derive :term-extraction.tags/PS   :term-extraction.tags/PRNM)  ;Pronome Possessivo
    (derive :term-extraction.tags/PD   :term-extraction.tags/PRNM)  ;Pronome Demonstrativo
    (derive :term-extraction.tags/PI   :term-extraction.tags/PRNM)  ;Pronome Indefinido
    (derive :term-extraction.tags/PL   :term-extraction.tags/PRNM)  ;Pronome Relativo
    (derive :term-extraction.tags/PP   :term-extraction.tags/PRNM)  ;Pronome Pessoal

    (derive :term-extraction.tags/NC   :term-extraction.tags/NU)    ;Número Cardinal
    (derive :term-extraction.tags/NM   :term-extraction.tags/NU)    ;Número Multiplicativo
    (derive :term-extraction.tags/NO   :term-extraction.tags/NU)    ;Número Ordinal
    (derive :term-extraction.tags/NR   :term-extraction.tags/NU)    ;Número Romano
    ))

