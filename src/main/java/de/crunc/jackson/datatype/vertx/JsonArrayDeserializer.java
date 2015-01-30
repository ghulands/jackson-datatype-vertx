package de.crunc.jackson.datatype.vertx;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.vertx.java.core.json.JsonArray;

import java.io.IOException;

/**
 * Deserializer which produces {@link JsonArray}.
 *
 * @author Hauke Jaeger, hauke.jaeger@googlemail.com
 * @since 2.2.2
 */
class JsonArrayDeserializer extends StdDeserializer<JsonArray> {
    private static final long serialVersionUID = 1L;

    public final static JsonArrayDeserializer instance = new JsonArrayDeserializer();

    JsonArrayDeserializer() {
        super(JsonArray.class);
    }

    @Override
    public JsonArray deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        JsonArrayBuilder builder = JsonArrayBuilder.array();

        JsonToken t;
        while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
            switch (t) {
                case START_ARRAY:
                    builder.add(deserialize(jp, ctx));
                    continue;
                case START_OBJECT:
                    builder.add(JsonObjectDeserializer.instance.deserialize(jp, ctx));
                    continue;
                case VALUE_STRING:
                    builder.add(jp.getText());
                    continue;
                case VALUE_NULL:
                    builder.addNull();
                    continue;
                case VALUE_TRUE:
                    builder.add(Boolean.TRUE);
                    continue;
                case VALUE_FALSE:
                    builder.add(Boolean.FALSE);
                    continue;
                case VALUE_NUMBER_INT:
                    builder.add(jp.getNumberValue());
                    continue;
                case VALUE_NUMBER_FLOAT:
                    builder.add(jp.getNumberValue());
                    continue;
                default:
                    throw ctx.mappingException("Unrecognized or unsupported JsonToken type: " + t);
            }
        }

        return builder.build();
    }
}