package tokyo.rev.elasticsearch.index.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;

public class ConcatenationTokenFilter extends TokenFilter {

  public static final int DEFAULT_MAX_OUTPUT_TOKEN_SIZE = 1024;
  private final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  private final PositionLengthAttribute posLenAtt = addAttribute(PositionLengthAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

  private List<String> terms = null;
  private final int maxOutputTokenSize;
  private AttributeSource.State finalState;

  private boolean inputEnded = false;

  public ConcatenationTokenFilter(TokenStream input) {
    this(input, DEFAULT_MAX_OUTPUT_TOKEN_SIZE);
  }

  public ConcatenationTokenFilter(TokenStream input, int maxOutputTokenSize) {
    super(input);
    this.maxOutputTokenSize = maxOutputTokenSize;
  }

  @Override
  public final boolean incrementToken() throws IOException {
    if (inputEnded) {
      return false;
    }
    boolean result = buildSingleOutputToken();
    finalState = captureState();
    return result;
  }

  private final boolean buildSingleOutputToken() throws IOException {
    inputEnded = false;

    char clonedLastTerm[] = null;
    terms = new ArrayList<String>();
    int outputTokenSize = 0;
    while (input.incrementToken()) {
      if (outputTokenSize > maxOutputTokenSize) {
        continue;
      }

      final char term[] = termAttribute.buffer();
      final String termStr = new String(term);
      final int length = termAttribute.length();

      if (!terms.contains(termStr)) {
        clonedLastTerm = new char[length];
        System.arraycopy(term, 0, clonedLastTerm, 0, length);
        if (terms.size() > 0) {
          outputTokenSize++;
        }
        terms.add(new String(clonedLastTerm));
        outputTokenSize += length;
      }
    }
    input.end();
    inputEnded = true;

    offsetAtt.setOffset(0, offsetAtt.endOffset());
    posLenAtt.setPositionLength(1);
    posIncrAtt.setPositionIncrement(1);
    typeAtt.setType("word");

    if (terms.size() < 1) {
      termAttribute.setEmpty();
      return false;
    }

    if (outputTokenSize > maxOutputTokenSize) {
      termAttribute.setEmpty();
      terms.clear();
      return false;
    }

    if (terms.size() == 1) {
      termAttribute.setEmpty().append(new String(clonedLastTerm));
      terms.clear();
      return true;
    }

    StringBuilder sb = new StringBuilder();
    for (String term : terms) {
      sb.append(term.toCharArray());
    }
    termAttribute.setEmpty().append(sb);
    terms.clear();
    return true;

  }

  @Override
  public final void end() throws IOException {
    if (!inputEnded) {
      input.end();
      inputEnded = true;
    }

    if (finalState != null) {
      restoreState(finalState);
    }
  }

  @Override
  public void reset() throws IOException {
    super.reset();
    inputEnded = false;
    terms = null;
  }
}
