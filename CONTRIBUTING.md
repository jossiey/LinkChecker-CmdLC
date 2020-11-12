# How to contribute for this repository

## Getting Started

- Fork this repository
- Git clone https://github.com/yourGitHubName/LinkChecker-CmdLC.git
- Report an issue or a bugs
- Modify the code
- Run `./staticAnalysis.bat` to do code reformatting and bug checking
- Write a clean and detailed Pull Request


## Development setup
The LinkChecker-CmdLC requires Java 8 or higher.


### Tools

#### Google Java Format
 The ![Google Java Format](https://github.com/google/google-java-format) is
 a tool that reformats Java source code to comply with Google Java Style 
 - install google-java-format Eclipse plugin: 
 >  Help > Eclipse Marketplace > Search > Find Google-Java-Format
 or
 - run `./staticAnalysis.bat` before you commit 
 
#### SpotBugs
 The ![SpotBugs](https://spotbugs.github.io/) is a tool that uses static analysis 
 to look for bugs in Java code
 - install SpotBugs Eclipse plugin:
 >  Help > Eclipse Marketplace > Search > Find SpotBugs
 or
 - run `./staticAnalysis.bat` before you commit


### Setup a Git Pre-Commit Hook
  Git automatically run staticAnalysis.bat on any changes that are being committed 
> run `cp ./staticAnalysis.bat .git/hooks/pre-commit` 
 

 
