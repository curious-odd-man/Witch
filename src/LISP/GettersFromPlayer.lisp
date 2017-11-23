;; TESTED
;; f() will call java method for GUI, and receive a number
;; verify number for valid value
(defun GetPlayerCount ()
    (setq ConstPlayerCount (jstatic "askNumber" "game.WitchGame" "Enter player count from 2 to 5:" 2))
    (cond
        ( (and (> ConstPlayerCount 1) (< ConstPlayerCount 6)) ConstPlayerCount)
        (t (GetPlayerCount))
	)
)

;; TESTED
;; f() will call java method for GUI, and receive a number
;; verify number for valid value
(defun GetHumanCount (InputPlayerCount)
    (setq ConstHumanCount (jstatic "askNumber" "game.WitchGame" (format nil "Enter count of human players from 1 to ~a" InputPlayerCount) 1))
    (cond
        ( (and (> ConstHumanCount 0) (< ConstHumanCount (+ InputPlayerCount 1))) ConstHumanCount)
        (t (GetHumanCount InputPlayerCount))
	)
)

;; TESTED
;; f() will wait until user presses button
(defun GetPressedButton (ButtonText)
   (let* (
      (class (jclass "game.WitchGame"))
      (intclass (jclass "int"))
      (method (jmethod class "waitButtonPress" intclass))
      (result (jcall method GlobalJavaGameObject ButtonText)))
   )
)

;; TESTED
;; f() will ask user to select a card from a set
(defun GetUserSelectedCardNum (CardNum)
   (cond 
      ((= 0 CardNum) nil)
      (t 
         (let* (
            (class (jclass "game.WitchGame"))
            (intclass (jclass "int"))
            (method (jmethod class "getUserSelectedCard" intclass))
            (result (jcall method GlobalJavaGameObject CardNum)))
            (setq Rslt result)
         )
      )
   )
   Rslt
)

;; f() will draw single card that is passed from one player to another
(defun DrawPassedCardInJava (Card)
    (let* 
        (
            (class (jclass "game.WitchGame"))
            (intclass (jclass "int"))
            (method (jmethod class "drawPassedCard" intclass intclass))
            (result (jcall method GlobalJavaGameObject (car Card) (cadr Card)))
        )
    )
)

(defun PronounceLooserInJava (PlayerName Score)
    (RemoveAllCardsFromDesk)
    (DEBUG "user to pronounce " (list PlayerName Score))
    (jstatic "pronounceLooser" "game.WitchGame" (format nil "~a LOST! Score: ~a" PlayerName Score))
)

(defun DrawPlayerTableJava (PlayerTable)
    (cond
        ((null PlayerTable) nil)
        (t  
            (let* 
                (
                    (CurrentID (cadar PlayerTable))
                    (CurrentScore (caddar PlayerTable))
                    (CurrentPlayerName (cadddr (car PlayerTable)))
                )
                (jstatic "addPlayerEntry" "game.WitchGame" (format nil "Score: ~a Name: ~a"  CurrentScore CurrentPlayerName))
            )
            (DrawPlayerTableJava (cdr PlayerTable))
        )
    )
)

(defun EndDrawPlayerTable ()
    (jstatic "endPlayerTable" "game.WitchGame")
)

(defun RemoveAllCardsFromDesk ()
    (let*
        (
            (class (jclass "game.WitchGame"))
            (method (jmethod class "removeAllCardsFromDesk"))
            (result (jcall method GlobalJavaGameObject))
        )
    )
)
