# Spring Boot backend for the Quote app
This application serves as the backend for the React frontend, which is available at:
https://github.com/edwinbulter/quote-web

When launched, the API can be tested in IntelliJ using the file quote_api_test.http, located at
https://github.com/edwinbulter/quote-server/tree/main/src/test/java/ebulter/quote/controller

Implemented features:
- A set of quotes will be requested at ZenQuotes and written in H2DB if the database is empty
- If ZenQuotes is unavailable, a fallback file with quotes is used to initialize the empty database
- Only unique quotes are written to the database:
  - by looking at the quoteText only, quotes are compared
  - if the new quoteText doesn't appear in the database, it is added
- When requesting a random quote, 'quote ids to exclude' can be sent in the body of the POST request to avoid sending the same quote again when requesting a random quote
- If the list with 'quote ids to exclude' exceeds the number of quotes in the database:
  - a set of quotes is requested at ZenQuotes, added to the database and a random new quote is returned 
  - if ZenQuotes is unable to deliver the quotes, a random quote without looking at the ids to exclude is returned
- Liking of quotes
  - Liked quotes will be written on an event stream
  - Liked quotes will get their likes field incremented
