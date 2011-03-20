jTranslate
========

jTranslate is a cross platform command line tool written in java for performing transformations/preprocessing textual input. 

Check out the contents of the test directory to see some sample grammar files and translator classes. A runnable jarfile is included, which would be run with the following command on the files in the test directory: "java -jar jtranslate.jar --source=test/src/ --output=test/translated/ --grammar=test/grammars/    --translators=test/translators/;test/translators2/"

## Warning
jTranslate is a very young project. For the the time being, the API and grammar file format will be changing without much concern for backwards compatibility.

## Grammar Files
Grammar files are where you are going to define regular expression based rules. Grammar files must use the '.jtg' extension. There are three types of rules, reference rules ,translation rules, and scripted translation. jTranslate will only search for instances of translation and scripted translation rules, reference rules are there to make other reference and translation rules easier to read.

### Reference Rules
	integer {
		[0-9]+
	}

	decimal {
		<integer>\.<integer>
	}

### Translation Rules
Translation rules are what jTranslate actually searches for in your input files. They are defined almost exactly like reference rules, but you must map each translation rule to the class name that will perform the translation. This class must be loadable via the -t/--translators argument.

	number -> NumberTranslator {
		<integer> | <decimal>
	}

### Scripted Translation Rules
If you do not want to rely on precompiled external classes to perform your translations, jTranslate supports beanshell scripting within grammar files. Your scripts are run in a context with the predefined variable "match" being the MatchResult.

        convert_long_to_int {
            <integer>L
        } -> {{
            String original = match.group(0);
            return original.substring(0, original.length() -1);
        }}

### Rewrite Blocks
jTranslate supports special rewrite blocks for instances where you are not performing advanced text manipulation, and just want to perform a simple search and replace on you input files.

	@rewrite {
		"#FOO" -> "bar";
		"foo" -> "bar";
	}

#### All together:

	/*
		Example grammar file
	*/
	@rewrite {
		"#FOO" -> "bar";
		"foo" -> "bar";
	}

	integer {
		[0-9]+
	}

	decimal {
		<integer>\.<integer>
	}

	number -> NumberTranslator {
		<integer> | <decimal>
	}




#### Notes on Grammar Files
- Java style block and line comments are supported
- Multiple grammar files are supported if the -g/--grammar argument is a directory
- Multiple rewrite blocks per grammar file are supported for organization purposes
- All whitespace in reference/translation rule definitions is ignored
- Reference/translation rules are currently compiled to SINGLE regular expressions, so any grouping in a reference rule will affect the grouping of any rules that use the reference (this will change in the future)

## Translator Classes
As mentioned before, each translator rule must be mapped to a translator class which implements the "jTranslate.Translator" interface. Translators must implement a single method, translate(MatchResult match). The entire match of each transltion rule is replaced with the output of the translate method of the mapped translator class.

	import jtranslate.Translator;

	import java.util.regex.MatchResult;

	public class NumberTranslator implements Translator
	{
		@Override
		public String translate(MatchResult mat){
			String fullMatch = mat.group(0);
			boolean isDecimal = fullMatch.indexOf(".") != -1;

			// for some reason we want to convert decimals to ints..
			String output = isDecimal ? fullMatch.substring(0, fullMatch.indexOf(".")) : fullMatch;

			return output;
		}
	}

## Source and Output Arguments
jTranslate will recurse through each file in source directory/subdirectories, translate the file, and save the new file to the output directory. Directory structure is preserved from the source directory to the output directory. These arguments are not required to be directories, but it is strongly suggested.

## Translators Argument
The -t/--translators argument is extremely flexible, and is used behind the scenes to create a URLClassLoader. However, "file:" is prepended to each path so remote URLs are not supported (this will change). If you need multiple paths, the -t argument can be a semicolon delimited list of paths.

## License
Copyright (c) 2011 Davis Clark <davis.clark@net-machine.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

