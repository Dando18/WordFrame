package front.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import front.MainApplication;

public class DictUtil {

	private DictUtil() {
	}

	public static String getDefinition(String word) throws IOException {
		if (word == null || "".equals(word.trim())) {
			return "";
		}

		String path = MainApplication.class.getResource("dict").getPath();
		URL url = null;
		try {
			url = new URL("file", null, path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (url == null)
			return "";

		IDictionary dict = new Dictionary(url);
		dict.open();
		WordnetStemmer ws = new WordnetStemmer(dict);

		String defs = "";
		POS[] posToSearch = { POS.ADJECTIVE, POS.ADVERB, POS.NOUN, POS.VERB};

		for (POS pos : posToSearch) {
			Set<String> stems = new HashSet<>(ws.findStems(word, pos));
			for (String stem : stems) {
				String tmp = getDefsPlusSyn(dict, stem, pos, ", ");
				if (tmp != null && !("".equals(tmp.trim()) && " ".equals(tmp.trim()))) {
					defs += "\n\t-" + tmp;
				}
			}
		}

		return defs;
	}

	@SuppressWarnings("unused")
	private static String getDefs(IDictionary dict, String word, POS pos) {
		IIndexWord idxWord = dict.getIndexWord(word, pos);
		if (idxWord == null)
			return null;
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord wordi = dict.getWord(wordID);
		return wordi.getSynset().getGloss().split(";")[0];
	}
	
	private static String getDefsPlusSyn(IDictionary dict, String word, POS pos, String del) {
		IIndexWord idxWord = dict.getIndexWord(word, pos);
		if (idxWord == null)
			return null;
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord wordi = dict.getWord(wordID);
		
		String syns = "\nsyn: ";
		Set<IWord> synonyms = new HashSet<> (wordi.getSynset().getWords());
		for(IWord w : synonyms) {
			syns += w.getLemma();
			if(!w.equals(synonyms.toArray()[synonyms.size()-1])) {
				syns += del;
			}
		}
		
		return wordi.getSynset().getGloss().split(";")[0] + syns;
	}

}
