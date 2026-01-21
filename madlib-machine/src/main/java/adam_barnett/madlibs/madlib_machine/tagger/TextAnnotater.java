package adam_barnett.madlibs.madlib_machine.tagger;

import edu.stanford.nlp.pipeline.CoreDocument;


/*
 * Uses Stanford CoreNLP for part-of-speech tagging and tokenization.
 * CoreNLP is © Stanford University and distributed under the GNU General Public License v3.
 * See: https://stanfordnlp.github.io/CoreNLP/
 */

/** Provided text is converted to CoreDocument object, allowing the CoreNLP library to annotate parts of speech for each word
 * @see TextAnnotationProperties*/
public class TextAnnotater {

    private final CoreDocument document;

    /** Constructor creates document object upon instantiation with each word and punctuation identified with a part of speech */
    public TextAnnotater(String text) {
        this.document = new CoreDocument(text);
        TextAnnotationProperties.getInstance().getPipeline().annotate(document);
    }

    public CoreDocument getDocument() {
        return document;
    }

}
