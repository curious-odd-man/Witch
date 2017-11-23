;; TESTED
;; f() will create list of lists for player cards
(defun HandsCreate(PlayerCount Hands)
    (cond
        ((= PlayerCount 1) (list `()))
        (t (append (list Hands) (HandsCreate (- PlayerCount 1) Hands)))
    )
)

;; TESTED
;; f() deals cards for players
(defun CardsDeal (Hands Deck)
   (DEBUG "Hands" Hands)
   (DEBUG "Deck" Deck)
   (DEBUG "===================" ':)
    (cond
        ((null (cdr Deck)) (append (cdr Hands) (list (append (car Hands) (list (car Deck))))))
        (t (CardsDeal (append (cdr Hands) (list (append (car Hands) (list (car Deck))))) (cdr Deck)))
    )
)

;; TESTED
;; f() will return 1 if player is Human, and 0 otherwise
(defun HumanOrGLaDOS (Count)
   (cond
      ((> Count 0) (list 1))
      (t (list 0))
   )
)

;; TESTED
;; f() will create list for AI/Players
(setq TEMP_ID 0)
(defun PlayerGetControlList (InputPlayerCount InputHumanCount Input)
    (DEBUG "(PlayerCount HumanCount Input)" (list InputPlayerCount InputHumanCount Input))
    (DEBUG "===================" ':)
    (cond
        ((= InputPlayerCount 1) (append Input (list 
            (append 
               (HumanOrGLaDOS InputHumanCount)
               (list (setq TEMP_ID (+ TEMP_ID 1))) 
               (list 0)
               (list (list (jstatic "askText" "game.WitchGame" (format nil "Enter player name:") (HumanOrGLaDOS InputHumanCount))))
            )
        ))) 
        (t (PlayerGetControlList (- InputPlayerCount 1) (- InputHumanCount 1) (append Input (list 
            (append 
               (HumanOrGLaDOS InputHumanCount) 
               (list (setq TEMP_ID (+ TEMP_ID 1))) 
               (list 0)
               (list (list (jstatic "askText" "game.WitchGame" (format nil "Enter player name:") (HumanOrGLaDOS InputHumanCount))))
            )
        ))))
    )
)

;; TESTED
;; f() will find duplicate cards, and remove it
(defun HandRemoveDuplicates(Card Hand)
    (DEBUG "Hand" Hand)
    (DEBUG "Card" Card)
    (DEBUG "===================" ':)
    (cond
        ((> Card 0)
            (cond
                ((null Hand) nil)
                ((= Card (caar Hand)) 
                  (jstatic "ReceiveCardFromLISP" "game.WitchGame" (car Hand))
                  (cdr Hand)
                )
                (t (cons (car Hand) (HandRemoveDuplicates Card (cdr Hand))))
            )
        )
        ((= Card 0)
            (cond
                ((null (cdr Hand)) Hand)
                (t (cond
                     ((> (length (cdr Hand)) (length (setq Temp (HandRemoveDuplicates (caar Hand) (cdr Hand)))))
                        (jstatic "ReceiveCardFromLISP" "game.WitchGame" (car Hand))
                        (HandRemoveDuplicates 0 Temp)
                     )
                     (t (append (list (car Hand)) 
                        (HandRemoveDuplicates 0 (cdr Hand)))
                     )
                   ) 
                )
            )
        )
    )
)

;; TESTED
;; f() will draw all hands calling java methods
;; Hands - list of hands (list of cards)
;; isDuplicates - 1 - Drawing pairs, 0 - Hands
(defun DrawHands (Hands isDuplicates)
	(cond
		((null Hands) nil)
		(t 	(jstatic "CreateHand" "game.WitchGame")
			(SendHandToJava (HandSort (car Hands)))
			(jstatic "DrawHand" "game.WitchGame" (+ 1 (- PlayerCount (length Hands))) PlayerCount isDuplicates 
            (cond 
               ((= DEBUG 1) 0)
               ((and (= ConstHumanCount 1) (= (+ 1 (- PlayerCount (length Hands))) 1)) 0)
               (t 1)
            )
         )
			(DrawHands (cdr Hands) isDuplicates)
        )
    )
)

;; PART OF HandSort();
;; f() gets minimal card of a Hand
(defun Min (Hand)
   ;(DEBUG "Min = " Hand)
   (cond
      ((null (cdr Hand)) (car Hand))
      (t (cond
            ((< (car (setq Temp (Min (cdr Hand)))) (caar Hand)) Temp)
            (t (car Hand))
         )
      )
   )
)

;; TESTED
;; f() will sort Hand (list of cards in ascending order by its rank
(defun HandSort (Hand)
   ;(DEBUG "=====================" ':)
   ;(DEBUG "HandSort = " Hand)
   (cond
      ((null (cdr Hand)) (list (car Hand)))
      (t (append (list (setq Card (Min Hand))) (HandSort (RemoveCard (car Card) Hand))))  ; cheaters :D
   )
)

;; PART of HandSort;
;; f() removes Card from Hand
(defun RemoveCard(Card Hand)
   (cond
       ((null Hand) nil)
       ((= Card (caar Hand)) (cdr Hand))
       (t (cons (car Hand) (RemoveCard Card (cdr Hand))))
   )
)

;; TESTED
;; f() sends Hand to java to display it there
(defun SendHandToJava (Hand)
   (cond 
      ((null Hand) nil)
      (t 
         (jstatic "ReceiveCardFromLISP" "game.WitchGame" (car Hand))
         (SendHandToJava (cdr Hand))
      )
   )
)

;; TESTED
;; f() will remove duplicates from all hands
(defun RemoveAndDrawDuplicates (Hands Current)
   (DEBUG "Removing Duplicates" Hands)
   (cond 
      ((= (setq Current (+ Current 1)) (+ 1 PlayerCount)) Hands)
      ((null Hands) nil)
      (t 
         (jstatic "CreateHand" "game.WitchGame")
         (DEBUG "Hands after HRD" (setq Hands (append (cdr Hands) (list (HandRemoveDuplicates 0 (car Hands))))))
         (jstatic "DrawHand" "game.WitchGame" Current PlayerCount 1 0)
         (RemoveAndDrawDuplicates Hands Current)
      )
   )
)

;; f() will check all hands for lonely witch
;; will return 0 if no lonely witches, else will return player ID of looser
(defun CheckAllHandsForLoneWitch (Hands CurrentPlayerInfoList)
   (DEBUG "CHECK ALL FOR LONELY WITCH Hands and CurrentID" (list Hands CurrentPlayerInfoList))
   (cond
      ((null Hands) 0)
      ((eq (CheckHandForLoneWitch (car Hands)) nil) (cadar CurrentPlayerInfoList))
      (t (CheckAllHandsForLoneWitch (cdr Hands) (cdr CurrentPlayerInfoList)))
   )
)

;; f() will return NIL if there is lonely witch, else will return Hand as it is
(defun CheckHandForLoneWitch (Hand)
    (DEBUG "Check Hand For Lonely witch: Hand" Hand )
   (cond
      ((and (null (cdr Hand)) (= (caar Hand) 666)) nil)
      (t Hand)
   )
)