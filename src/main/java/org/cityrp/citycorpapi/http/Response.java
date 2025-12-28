package org.cityrp.citycorpapi.http;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

    private final Headers headers;
    private int statusCode;
    private T result;
}
