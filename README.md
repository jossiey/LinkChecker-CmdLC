# LinkChecker-CmdLC
#### This repository hosts a command-line tool finding and checking the broken URLs in multiple files.

## Features
* Finding all URLs from one or multiple given files base on the protocol of "http://" and "https://"
* Removing duplicated URLs from the given files
* Returning the URLs with different response code: 200 - Good, 400/404 - Bad, others - Unknown
* Coloring the output base on response code (Unix-based OS): Good - Green, Bad - Red, Unknown -  grey
* Redirecting to new location when response code is 301, 307, 308
* Providing help and version utility on Picocli open source tool
* Allowing to pass directory paths and recursively process all children under that directory
* Program exit with an appropriate code, if there are no bad links, exit with 0; else exit with 1
* Allowing to choose JSON format output by '-j', '-json', '/j', '\j' options in command line

## How it works
Download the LinkChecker-CmdLC.jar file from [Maven Central Repository](https://search.maven.org/search?q=g:com.github.jossiey)

#### On Unix-based operating systems
 1. Make an alias first
 > `alias lc='java -cp "picocli-4.5.1.jar:CmdLC.jar" osd.CmdLC'`
 2. Use **_lc_** command directly
 > `lc [-hV] <file1> <file2> ...`
 3. Demonstration on Linux 
 > ![presentation](https://jyangblogs.files.wordpress.com/2020/09/qrcl5800.gif)
 
#### On Windows operating systems
   Run .jar directly
 > `java -jar LinkChecker-CmdLC.jar <file1> <file2> ...`
 
## Available Options
| Options | Description |
| ---| ---|
| -h, --help | Show this help message and exit |
| -V, --version | Print version information and exit |
| -j, -json | Choose to print out the result in JSON format|
| -u, -url | Check a website URL directly |

## Contribution
Want to contribute? Great! First, read [CONTRIBUTING.md](https://github.com/jossiey/LinkChecker-CmdLC/blob/master/CONTRIBUTING.md)

## Using the Library
https://github.com/remkop/picocli
 
