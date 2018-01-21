# CodeItLater

## Summary
Make flags in source code where may have problems or can optimize. codeitlater help you track this flags and fix them in future.

## Features

* get comments in source code
* get comments depending on different key words
* get comments in special path
* can expand to other languages

## How to use

Write code as usual. The comment line that you want to leave mark in, left `:=` symbol after comment symbol.

For example:

Golang:
```golang
// /user/src/main.go
// test codeitlater
//:= this line can be read by codeitlater
//:= MARK: you can left keyword to marked comment line

```

Run `jar` file
`java -jar /path/to/codeitlater.jar -d /user/src/`

Or you have ran install method:
`codeitlater -d /user/src/`

You will get:
```
|-- /user/src/main.go
  |-- (3 "this line can be read by codeitlater")
  |-- (4 "MARK: you can left keyword to marked comment line")
```

Filter keyword (use -k be keyword flag, check out more flags by -h):
`java -jar /path/to/codeitlater.jar /user/src/ -k MARK`

You will get:
```
|-- /user/src/main.go
  |-- (4 "MARK: you can left keyword to marked comment line")
```

Python:
```python
# /src/main.py
# this line wont be read
#:= this line for codeitlater
print("aaa") ###:= this line can be read again
```

Run `codeitlater`

You will get:
```
|-- /src/main.py
  |-- (3 "this line for codeitlater")
  |-- (4 "this line can be read again")
```

## How to install

I wound like use home-brew to install this, unfortunately, I dont know ruby

require:

+ [Leiningen](https://leiningen.org)

download whole repository and install:

1. `lein uberjar`
2. `ln -sfv $(PWD)/target/*standalone.jar /usr/local/bin/codeitlater.jar`
3. `ln -sfv $(PWD)/run.sh /usr/local/bin/codeitlater`

uninstall:

1. `rm /usr/local/bin/codeitlater /usr/local/bin/codeitlater.jar`
2. delete all files (this repository && `~/.m2` is where dependencies download, if you want to delete too)
