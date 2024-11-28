# Changelog

## 1.1.0
* Added Spring Security and secure routes
* Added CORS settings
* Changed password encoder to Security one (Bcrypt)
* Timestamp in ApiError changed to long from Date
* ApiError `toString` returns JSON format in String
* Added new error handlers in `ExceptionHandlers`
* Refactor `AuthUtils` to follow Spring Security best strategies
* Updated `NOTNULL`, `EMAIL`, `PASSWORD` error messages.
* Added error "Token is expired".
* And much more...