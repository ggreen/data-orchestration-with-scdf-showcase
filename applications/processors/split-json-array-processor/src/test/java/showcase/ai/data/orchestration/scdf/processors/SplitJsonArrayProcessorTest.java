package showcase.ai.data.orchestration.scdf.processors;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Split input JSON array to individual messages
 * @author gregory green
 */
class SplitJsonArrayProcessorTest {

    private final SplitJsonArrayFunction subject = new SplitJsonArrayFunction();
    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void givenJsonArrayThenReturnMessages() throws JsonProcessingException {
//
//        var given = """
//                [{"id" : "1"},{"id" : "2"}]
//                """;
//        var actual = subject.apply(given);
//
//        assertThat(actual).isNotNull();
//        assertThat(actual.toStream().toArray().length).isEqualTo(2);
//
//        actual.subscribe(item ->
//            {
//                System.out.println(item);
//                assertThat(item).isNotEmpty();
//                assertThat(item).isInstanceOf(String.class);
//                try {
//                    objectMapper.readTree(item.toString());
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//    }
//
//    @Test
//    void givenJsonThenReturnMessages() throws JsonProcessingException {
//
//        var given = """
//                {"id" : "1"}
//                """;
//        var actual = subject.apply(given);
//
//        assertThat(actual).isNotNull();
//        assertThat(actual.toStream().toArray().length).isEqualTo(1);
//
//        actual.subscribe(item ->
//        {
//            System.out.println(item);
//            assertThat(item).isNotEmpty();
//            assertThat(item).isInstanceOf(String.class);
//            assertThat(item).contains("id");
//            try {
//                objectMapper.readTree(item.toString());
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    @Test
//    void givenNullOrEmptyThenReturnNothing() {
//        assertThat(subject.apply(null)).isNull();
//        assertThat(subject.apply("")).isNull();
//        assertThat(subject.apply("  ")).isNull();
//    }
//
//    @Test
//    @EnabledIfSystemProperty(named = "runSplitIntTest", matches = "true")
//    void integrationTest() throws IOException {
//
//        var json = IO.readFile(new File("src/test/resources/secret/boxscore.json"));
//
//        var actual = subject.apply(json);
//
//        assertThat(actual.toStream().toList()).isNotEmpty();
//
//        for (var item : actual.toStream().toList()){
//            System.out.println(item);
//            assertThat(item).isNotEmpty();
//            assertThat(item).isInstanceOf(String.class);
//        }
//
//    }
}