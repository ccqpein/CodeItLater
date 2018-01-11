# CodeItLater

## Summary
Make flags in source code where may have problems or can optimize. codeitlater help you track this flags and fix them in future.

## Features

* get comments in source code
* get comments depending on different key words
* get comments in special path
* can expand to other lanuages

## How to use

Write code as usual. The comment line that you want to leave mark in, left `:=` symbol after comment symbol.

For example:

Golang:
```golang
// /user/src/main.go
// test codeitlater
//:= this line can be read by codeitlater
//:= MARK: you can left kayword to marked comment line

```

Run `jar` file
`java -jar /path/to/codeitlater.jar /user/src/`

You will get:
```
|-- /user/src/main.go
  |-- (3 "this line can be read by codeitlater")
  |-- (4 "MARK: you can left kayword to marked comment line")
```

Filter keyword (use -k be keyword flag, check out more flags by -h):
`java -jar /path/to/codeitlater.jar /user/src/ -k MARK`

You will get:
```
|-- /user/src/main.go
  |-- (4 "MARK: you can left kayword to marked comment line")
```


