package name.lemerdy.eric.dataMunging.abstractdesign;

import com.google.common.base.CharMatcher;
import com.google.common.io.LineProcessor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Splitter.on;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.io.Resources.readLines;

public class MinimumDifferenceInFile {
    static <T> T minimum(URL file, ValuesFactory<T> factory) throws IOException {
        List<T> extrema = readLines(file, UTF_8, new LineProcessor<List<T>>() {
            List<T> result = newArrayList();

            @Override
            public boolean processLine(String line) throws IOException {
                List<String> datas = on(' ').omitEmptyStrings().trimResults(new CharMatcher() {
                    @Override
                    public boolean matches(char c) {
                        return c == '-' || c == ' ';
                    }
                }).splitToList(line);
                if (datas.isEmpty()) {
                    return true;
                }
                Optional<T> data = factory.create(datas);
                if (!data.isPresent()) {
                    return true;
                }
                return result.add(data.get());
            }

            @Override
            public List<T> getResult() {
                return result;
            }
        });
        return extrema.stream()
                .sorted()
                .findFirst()
                .get();
    }
}
