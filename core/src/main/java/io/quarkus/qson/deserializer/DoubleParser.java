package io.quarkus.qson.deserializer;

public class DoubleParser implements QsonParser {
    @Override
    public ParserState startState() {
        return ObjectParser.PARSER.startNumberValue;
    }

    @Override
    public <T> T getTarget(ParserContext ctx) {
        return (T)Double.valueOf(ctx.popDoubleToken());
    }
}
