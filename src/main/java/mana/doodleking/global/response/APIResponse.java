package mana.doodleking.global.response;

import lombok.Getter;

@Getter
public class APIResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    private APIResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                data
        );
    }

    public static <T> APIResponse<T> failure(ResultCode resultCode, String message) {
        return new APIResponse<>(
                resultCode.getCode(),
                message,
                null
        );
    }
}
