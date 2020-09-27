# LinkChecker-CmdLC
##### This repository hosts a command-line tool finding and checking the broken URLs in a file.

## Features
* Finding all URLs from the given file base on the protocol of "http://" and "https://"
* Removing duplicated URLs from the given file
* Returning the URLs with different response code: 200 - Good, 400/404 - Bad, others - Unknown
* Coloring the output base on response code (Unix-based OS): Good - Green, Bad - Red, Unknown -  grey
* Redirecting to new location when response code is 301, 307, 308
* Providing help and version utility on Picocli open source tool

## How it works
Download the CmdLC.jar file inside release0.1 folder

#### On Unix-based operating systems
 1. Make an alias first
 > `alias lc='java -cp "picocli-4.5.1.jar:CmdLC.jar" osd.CmdLC'`
 2. Use **_lc_** command directly
 > `lc [-hV] <file>`
 
#### On Windows operating systems
   Run .jar directly
 > `java -jar CmdLC.jar <file>`
 
## Available Options
| Options | Description |
| ---| ---|
| -h, --help | Show this help message and exit |
| -V, --version | Print version information and exit |

## Using the Library
https://github.com/remkop/picocli
 
