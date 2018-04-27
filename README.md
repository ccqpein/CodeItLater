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

then run `codeitlater` in code root path 

You will get:

```
|-- /user/src/main.go
  |-- (3 "this line can be read by codeitlater")
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


**Give specify file type**

```
codeitlater -f clj
```

You will get result only from clojure.

```
codeitlater -f "clj py"
```

Then results both of clojure and python will return.


#### Specific path ####

Run `codeitlater -d /user/src/` let codeitlater just scan specific path.

#### Filter keyword ####

Filter keyword (use -k be keyword flag, check out more flags by -h):
`codeitlater -k MARK`

You will get:

```
|-- /user/src/main.go
  |-- (4 "MARK: you can left keyword to marked comment line")
```

## How to install

I wound like use home-brew to install this, unfortunately, I dont know ruby

require:

+ [Leiningen](https://leiningen.org)
+ JDK/JRE for leiningen

download whole repository to whereever you want && install under folder path:

1. `lein uberjar`
2. `ln -sfv $(PWD)/target/*standalone.jar /usr/local/bin/codeitlater.jar`
3. `ln -sfv $(PWD)/run.sh /usr/local/bin/codeitlater`

uninstall:

1. `rm /usr/local/bin/codeitlater /usr/local/bin/codeitlater.jar`
2. delete all files (this repository && `~/.m2` is where dependencies download, if you want to delete too)

update:

I do not write update method so far, just reinstall all stuff.

## Expand other languages ##

Codeitlater support languages those only I code, Clojure, Python, Golang, Rust, and so on. If you would like code other languages. Make your own json file like [this](https://raw.githubusercontent.com/ccqpein/codeitlater/master/src/codeitlater/comments.json). Then use `-j` to input your own json dict. 

`--jsonx` used to add/expand new dict to current one. It does not change current dict, just add new.

`-j` and `--jsonx` can work together.

**Caution:** all json files you made up should be real json, can pass json-parser check, otherwise codeitlater will panic. 
Like I make up a totally empty json file (no contents inside), it will panic. But if it including a `{}` (empty json), it works fine.
