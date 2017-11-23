;; IN DEVELOPMENT
;; HERE ALL FUN BEGINS
(defun StartWitchGame ()
    (setq ConstPlayerCount 0)
    (setq ConstHumanCount 0)
    (setq PlayerInfoList `())

    ;Remembers GlobalJavaGameObject for non-static function calls from game.WitchGame class
    (setq GlobalJavaGameObject (jstatic "launchGame" "game.WitchGameMain"))
    
    ;Get Player Count
    (cond 
        ((= DEBUG 1) (setq ConstPlayerCount 5))
        (t (setq ConstPlayerCount (GetPlayerCount)))
    )
    
    
    ;Get Human Player Count
    (cond 
        ((= DEBUG 1) (setq ConstHumanCount 1))
        (t (setq ConstHumanCount (GetHumanCount ConstPlayerCount)))
    )
    
    ;Creating list of AI/Humans
    (setq PlayerInfoList (PlayerGetControlList ConstPlayerCount ConstHumanCount '()))
    (DEBUG "Player info list" PlayerInfoList)
    
    (InitNewGame)
)

(defun InitNewGame ()
    (DEBUG "NEW GAME STARTS" `())
    (setq PlayerCount ConstPlayerCount)
    (setq PlayerInfoListForAGame PlayerInfoList)

    (EndDrawPlayerTable)
    (DrawPlayerTableJava PlayerInfoListForAGame)
    
    
    ;Get Shuffeled Deck
    (setq Deck (DeckGetShuffled))
    (DEBUG "Shuffled Deck" Deck)
    
    ;Creating hands (with dealed cards) for players
    (setq Hands (CardsDeal (HandsCreate PlayerCount `()) Deck))

    (DEBUG "Dealed Cards in Hands" Hands)
     
    (DrawHands Hands 0)
    (GetPressedButton 0)
    (RemoveAllCardsFromDesk)

    (setq Hands (RemoveAndDrawDuplicates Hands 0))
    (DrawHands Hands 0)
    (GetPressedButton 0)

    (RemoveAllCardsFromDesk)
    (DrawHands Hands 0)
    (EndDrawPlayerTable)
    (DrawPlayerTableJava PlayerInfoListForAGame)
    
    
    (cond
        ((= 0 (setq LooserID (CheckAllHandsForLoneWitch Hands PlayerInfoListForAGame))) (MakeATurn))
        (t 
            (DEBUG "Adding looser to InfoList" (list LooserID PlayerInfoList))
            (setq PlayerInfoList (Loosers LooserID PlayerInfoList))
        )
    )
    (DEBUG "Added looser to InfoList" PlayerInfoList)
    (InitNewGame)
)

;; TESTED
;; f() incremets lost count for player with LooserID
(defun Loosers (LooserID PlayerInfoList)
    (DEBUG "Searching Looser" (list LooserID PlayerInfoList))
    (setq CurrentID (cadar PlayerInfoList))
    (setq CurrentScore (caddar PlayerInfoList))
    (setq CurrentStatus (caar PlayerInfoList))
    (setq CurrentPlayerName (cadddr (car PlayerInfoList)))
    (cond
        ((= LooserID CurrentID) 
            (setq CurrentTempPlayer (list CurrentStatus CurrentID (+ 1 CurrentScore) CurrentPlayerName))
            (DEBUG "Found: " CurrentTempPlayer)
            (PronounceLooserInJava CurrentPlayerName (+ CurrentScore 1))
            (append (list CurrentTempPlayer) (cdr PlayerInfoList))
         )
        (t (append (list (car PlayerInfoList)) (Loosers LooserID (cdr PlayerInfoList))))
    )
)

;; GAME LOOP
;; All game processing is maintained here
;; PlayerInfoList - contains lists (x y z (a)), 
;;       x ={0, 1} - 1:Human Player
;;       y - Player ID
;;       z - Lost counter
;;       a - Player name
;; Hands - all hands with cards
;; PlayerCount, HumanCount
;; ChoosenCard - card that is passed from one player to another; is filled in RemoveCardByIndex
(defun MakeATurn ()
    (EndDrawPlayerTable)
    (DrawPlayerTableJava PlayerInfoListForAGame)
    
    (setq PlayerThatGivesCard (GetLastHand Hands))
    (DEBUG "GotLastHand" PlayerThatGivesCard)

    (cond 
      ((= (caar PlayerInfoListForAGame) 0) (setq CardToGive (random (length PlayerThatGivesCard)))) ; AI turn
      (t (setq CardToGive (GetUserSelectedCardNum (length PlayerThatGivesCard))))   ; human player turn
    )

    (setq PlayerThatGivesCard (RemoveCardByIndex PlayerThatGivesCard CardToGive -1))
    (DEBUG "Removed Card" PlayerThatGivesCard)
    (setq Hands (ReplaceLastHand Hands PlayerThatGivesCard))
    (cond
      ((> PlayerCount (length (setq Hands (DeleteEmptyHands Hands))))  
         (setq PlayerInfoListForAGame (cdr PlayerInfoListForAGame))
      )
      (t nil)
    )
    (setq PlayerCount (length Hands))
    (DEBUG "Removed Card Hands" Hands)
    (DEBUG "Card to add" ChoosenCard)
    (DrawPassedCardInJava ChoosenCard)
    (setq PlayerThatTakesCard (append (car Hands) (list ChoosenCard)))
    (setq PlayerThatTakesCard (RemoveAndDrawDuplicates (list PlayerThatTakesCard) 0))
    (setq Hands (append (cdr Hands) PlayerThatTakesCard ))
    (DEBUG "Finish" Hands)
    (cond
      ((> PlayerCount (length (setq Hands (DeleteEmptyHands Hands))))  
         (setq PlayerInfoListForAGame (cdr PlayerInfoListForAGame))
      )
      (t (setq PlayerInfoListForAGame (append (cdr PlayerInfoListForAGame) (list (car PlayerInfoListForAGame)))))
    )
    (DEBUG "No Empty Hands" Hands)
    (DEBUG "Scrolled GameInfoList" PlayerInfoListForAGame)

    (setq PlayerCount (length Hands))

    (GetPressedButton 0)
    (RemoveAllCardsFromDesk)
    (DrawHands Hands 0)   

    ; check if make next turn
    (DEBUG "Length Hands" (length Hands))
    (cond
      ((= (length Hands) 1) (setq PlayerInfoList (Loosers (cadar PlayerInfoListForAGame) PlayerInfoList)))
      (t (MakeATurn))
    )
)

(defun DeleteEmptyHands (TmpHands)
   (DEBUG "EmptyHands" TmpHands)
   (cond
      ((null (cadr TmpHands)) (list (car TmpHands)))
      (t (append (list (car TmpHands)) (DeleteEmptyHands (cdr TmpHands))))
   )
)

(defun DephtToId (Idx InfoList Current)
   (setq Current (+ 1 Current))
   (cond
      ((= Idx Current) (cadar InfoList))
      (t (DepthToId Idx Current (cdr InfoList)))
   )
)

(defun ReplaceLastHand (Hands Hand)
   (cond
      ((null (cdr Hands)) (list Hand))
      (t (append (list (car Hands)) (ReplaceLastHand (cdr Hands) Hand)))
   )
)

(defun RemoveCardByIndex (Hand Idx CurrentPosition)
   (setq CurrentPosition (+ 1 CurrentPosition))
   (DEBUG "Removing Card" (list Hand Idx CurrentPosition))
   (cond
      ((= Idx CurrentPosition) 
         (setq ChoosenCard (car Hand))
         (cdr Hand)
      )
      (t  (append (list (car Hand)) (RemoveCardByIndex (cdr Hand) Idx CurrentPosition)))
   )
)

;; f() will return last hand
(defun GetLastHand (Hands)
   (cond
      ((null (cdr Hands)) (car Hands))
      (t     (GetLastHand (cdr Hands)))
   )
)

