;; Constants
(setq ConstDeck  '(
             (6 0) (7 0) (8 0) (9 0) (10 0) (11 0) (12 0) (13 0) (14 0)
             (6 1) (7 1) (8 1) (9 1) (10 1) (11 1) (12 1) (13 1) (14 1)
             (6 2) (7 2) (8 2) (9 2) (10 2) (11 2) (13 2) (14 2)
             (6 3) (7 3) (8 3) (9 3) (10 3) (11 3) (666 3) (13 3) (14 3) 
            )
)
(setq ConstMask (list  0 1 2 3 4 5 6 7 8 9
                    10 11 12 13 14 15 16 17 18 19
                    20 21 22 23 24 25 26 27 28 29
                    30 31 32 33 34
                    ) 
)


;; TESTED
;;(x y): x -> Rank; y -> {0 - Diamonds, 1 - Hearts, 2 - Clubs, 3 - Spades}
(defun DeckGetShuffled ()
    (setq Deck ConstDeck)
    (setq Mask (DeckMaskGetShuffled))
    (setq Deck (DeckArrangeByMask Deck Mask))
)

;; TESTED
;; f() will get element from Input on Position
;; e.g.: Output = Input[Position];
(defun DeckGetCardFromPosition (Input Position)
   (cond
      ((null (car Input)) (DEBUG "ERROR in DeckGetCardFromPosition" Input));should never get here
      ((eq Position 0) (list (car Input)))
      (t (DeckGetCardFromPosition (cdr Input) (- Position 1)))
   )
)

;; TESTED
;; f() will arrange elements from input in order provided by mask
;; e.g.: Output[10] = Input[Mask[10]];
(defun DeckArrangeByMask (Input Mask)
   (cond
      ((null (cdr Mask)) (DeckGetCardFromPosition Input (car Mask)))
      (T (append (DeckGetCardFromPosition Input (car Mask)) (DeckArrangeByMask Input (cdr Mask))))
   )
)

;; TESTED
(defun DeckMaskGetShuffled ()
   (setq   Input ConstMask)
   (setq RandomCount (random 9) )
   (loop for i from 0 to RandomCount do
      (setq Input
         (sort Input #'(lambda (x y)
               (declare (ignore x y))
               (zerop (random 2))
            )
         )
      )
   )
   (setq ConstMask Input)   ; PATCH TO SORT ERRORS
   Input
)