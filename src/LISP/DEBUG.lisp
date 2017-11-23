;; TESTED
(setq DEBUG 1)
(defun DEBUG (msg param)
    (cond 
        ((= DEBUG 1) (format t "LISP_DEBUG ~a: ~a~%" msg param))
    )
)

(defun TestGetUserSelectedCardNum (Hands)
   (cond 
      ((null Hands) nil)
      (t 
         (GetUserSelectedCardNum (length (car Hands)))
         (TestGetUserSelectedCardNum (cdr Hands))
      )
   )
)