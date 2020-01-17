package se.alten.schoolproject.exceptions;

public class GeneralException extends Exception {

        public GeneralException(String errorMessage,
                              Throwable error) {
            super (errorMessage, error);
        }
}
