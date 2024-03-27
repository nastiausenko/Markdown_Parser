# Markdown Parser

This console application takes as input the path to a Markdown text file and generates an HTML markup fragment from it. The 
application outputs the generated HTML markup either to the standard output (stdout) or, with the --out /path/to/output.html 
argument, to the output file. If the markup in the input file is incorrect, the application prints the error to the standard error 
(stderr) and exits with a non-zero exit code.

## Requirements

You must have Java installed on your machine to run this application.

## Installation

1. Clone this repository with the command
```
git clone https://github.com/nastiausenko/Markdown_Parser.git
```

## Usage

1. Compile Java files
```
javac src/main/java/org/example/*.java 
```

2. Run the application
```
java -cp src/main/java org.example.Main <file path>
```

> **NOTE:** set the --out flag to write the converted fragment to the specified file

```
java -cp src/main/java org.example.Main <file path> --out <output file path>
```

## Example

Markdown
```
Я із **надій** будую човен,
І вже немовби наяву
З `тобою, ніжний, срібномовен`,
По морю радості пливу.

І гомонять навколо `хвилі`,
З _бортів човна змивають мох_,
І ми з тобою вже не в силі
**Буть нещасливими удвох**.

\```
І ти **ясна**, і я `прозорий`,
_І душі наші мов пісні_,
І світ **_великий, неозорий_**
Належить нам – тобі й мені.
\```

О **мо_ре** радості безкрає,
Чи я тебе `перепливу`?
Якби того, що в _мріях маю_,
**Хоч краплю мати наяву**.
```
HTML
```
<p>Я із <b>надій</b> будую човен,</p>
<p>І вже немовби наяву</p>
<p>З <tt>тобою, ніжний, срібномовен</tt>,</p>
<p>По морю радості пливу.</p>
<p>І гомонять навколо <tt>хвилі</tt>,</p>
<p>З <i>бортів човна змивають мох</i>,</p>
<p>І ми з тобою вже не в силі</p>
<p><b>Буть нещасливими удвох</b>.</p>
<p><pre>
І ти **ясна**, і я `прозорий`,
_І душі наші мов пісні_,
І світ **_великий, неозорий_**
Належить нам – тобі й мені.
</pre></p>
<p>О <b>мо_ре</b> радості безкрає,</p>
<p>Чи я тебе <tt>перепливу</tt>?</p>
<p>Якби того, що в <i>мріях маю</i>,</p>
<p><b>Хоч краплю мати наяву</b>.</p>
```
## Revert commit

### [Link](https://github.com/nastiausenko/Markdown_Parser/commit/f5500d10ccecd8ec33b78dff2a84c7c9b854d7a8)
