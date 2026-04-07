package murphy.springframework.authservice.dto.item;

import murphy.springframework.authservice.common.ItemState;

public record ItemResponse(String id, String data, String userId, ItemState itemState) {
}
