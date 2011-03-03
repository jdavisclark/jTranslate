package jtranslate;

import de.susebox.jtopas.TokenizerException;
import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarSet;
import jtranslate.parser.GrammarParser;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedList;

public class JTranslate
{
    public static Options buildOptions()
    {
        Options opts = new Options();

        Option grammar = new Option("g", "grammar", true, "Input grammar file or input grammer directory");
        grammar.setValueSeparator('=');
        grammar.setRequired(true);
        opts.addOption(grammar);

        Option source = new Option("s", "source", true, "Input source file or source file directory");
        source.setValueSeparator('=');
        source.setRequired(true);
        opts.addOption(source);

        Option output = new Option("o", "output", true, "Output directory");
        output.setValueSeparator('=');
        output.setRequired(true);
        opts.addOption(output);


        Option translator = new Option("t", "translators", true, "Path to class file, jar file, or directory of both. If this is a directory, the path must have a trailing file separator. May specify a list of paths delimited by ';'");
        translator.setValueSeparator('=');
        translator.setRequired(true);
        opts.addOption(translator);

//        Option config = new Option("c", "config", true, "Configuration file.");
//        config.setValueSeparator('=');
//        opts.addOption(config);

        opts.addOption("h", "help", false, "Display usage information");

        return opts;
    }

    public static CommandLine parseArgs(Options opts, String[] args) throws ParseException {
        return new GnuParser().parse(opts, args);
    }

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, TokenizerException
    {
        Options opts = buildOptions();
        CommandLine cl = null;
        HelpFormatter help = new HelpFormatter();

        try {
            cl = parseArgs(opts, args);
        }
        catch(ParseException e) {
            help.printHelp("JExtend", "Must specify grammar(s) path, source(s) path, and translator(s) path!", opts, "", true);
            System.exit(1);
        }

        if(cl.hasOption("h")) {
            help.printHelp("JExtend", opts);
            return;
        }


		LinkedList<File> grammerFiles = getGrammarFiles(cl.getOptionValue("g"));
        GrammarSet set = parseGrammarRules(grammerFiles);
		JTranslateEnvironment env = new JTranslateEnvironment(set);
        loadTranslatorClasses(env, cl);

		String sep = System.getProperty("file.separator").trim();
        String sourcePath = cl.getOptionValue("s");
	    String outputDir = "";
        File srcFile = new File(sourcePath);

        long start = System.currentTimeMillis();

        if(cl.hasOption("o"))
            outputDir = cl.getOptionValue("o");
        else if(srcFile.isDirectory()) {
            outputDir = srcFile.getCanonicalPath();
        }
        else {
            outputDir = new File(sourcePath).getParent();
        }

        File outputDirFile = new File(outputDir);
        if(!outputDirFile.exists()) {
            outputDirFile.mkdir();
        }

        if(srcFile.isDirectory())
        {
            for(File src : FileUtils.listFiles(srcFile, null, true)) {
                translate(src, outputDirFile, sep, env, cl);
            }
        }
        else {
            translate(srcFile, outputDirFile, sep, env, cl);
        }
        long end = System.currentTimeMillis();
        long seconds = (end - start)/1000;
        long remain = (end - start)%1000;
        System.out.println("\n\nTranslation Duration: "+seconds+"."+remain+" seconds");
	}

    public static void translate(File source, File outputDir, String sep, JTranslateEnvironment env, CommandLine cl) throws IOException
    {
        System.out.println("Translating file: "+source.getCanonicalPath());
        File srcRoot = new File(cl.getOptionValue("s"));
        String subPath = srcRoot.toURI().relativize(source.toURI()).getPath();

        File outputFile = new File(outputDir, subPath);
        File outputParent = outputFile.getParentFile();
        if(!outputParent.exists())
            outputParent.mkdirs();
        outputFile.createNewFile();

        FileOutputStream fout = new FileOutputStream(outputFile);
        fout.write(env.translate(source).getBytes());
        fout.close();
        System.out.println("\t\t->\t"+outputFile.getPath());
    }

    public static void loadTranslatorClasses(JTranslateEnvironment env, CommandLine cl) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String tArg = cl.getOptionValue("t");
        tArg = tArg.endsWith(";") ? tArg.substring(0, tArg.length()-1) : tArg;
        String[] tArgs = tArg.split(";");
        URL[] urls = new URL[tArgs.length];
        String sep = System.getProperty("file.separator");
        for(int i = 0; i<urls.length; i++) {
            String path = FilenameUtils.normalize(tArgs[i]);
            urls[i] = new URL("file:"+path);
        }

        URLClassLoader loader = new URLClassLoader(urls);

        env.registerTranslators(loader);
    }

    public static LinkedList<File> getGrammarFiles(String gPath)
    {
        File f = new File(gPath);
        LinkedList<File> grammars = new LinkedList<File>();
        if(f.isDirectory()) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jtg");
                }
            };
            grammars.addAll(Arrays.asList(f.listFiles(filter)));
        }
        else {
            grammars.add(f);
        }

        return grammars;
    }

    public static GrammarSet parseGrammarRules(Iterable<File> gFiles) throws FileNotFoundException, TokenizerException
    {
        GrammarParser parser = new GrammarParser();
        GrammarSet set = new GrammarSet();
        for(File f : gFiles) {
            set.addSet(parser.parse(f));
        }

        return set;
    }

}

