package tokyo.rev.elasticsearch.plugin.analysis;

import org.elasticsearch.index.analysis.TokenFilterFactory;
import tokyo.rev.elasticsearch.index.analysis.ConcatenationTokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.Map;

import static java.util.Collections.singletonMap;

public class ConcatenationTokenFilterPlugin extends Plugin implements AnalysisPlugin {
    @Override
    public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return singletonMap("concatenation", ConcatenationTokenFilterFactory::new);
    }
}
