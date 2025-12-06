package lab.is.controllers;

import lab.is.exceptions.UserDoesNotHaveEnoughRightsException;
import lab.is.security.bd.entities.RoleEnum;
import lab.is.security.model.UserDetailsImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lab.is.dto.responses.insertion.history.WrapperListInsertionHistoriesResponseDto;
import lab.is.services.insertion.history.InsertionHistoryService;
import lombok.RequiredArgsConstructor;

@Profile({"dev", "helios", "default"})
@RestController
@RequestMapping("/api/v1/insertion/histories")
@RequiredArgsConstructor
public class InsertionHistoryController {
    private final InsertionHistoryService insertionHistoryService;

    @GetMapping
    public ResponseEntity<WrapperListInsertionHistoriesResponseDto> getAll(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(required = false) Long userId,
        @PageableDefault(page = 0, size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (userId == null &&
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority(RoleEnum.ROLE_ADMIN.name()))) {
            return ResponseEntity.ok(
                insertionHistoryService.findAll(pageable)
            );
        }
        if (userId == null) {
            throw new UserDoesNotHaveEnoughRightsException("у пользователя недостаточно прав");
        }
        Long userIdFromUserDetails = userDetails.getId();
        if (!userId.equals(userIdFromUserDetails)) {
            throw new UserDoesNotHaveEnoughRightsException("у пользователя недостаточно прав");
        }
        return ResponseEntity.ok(
            insertionHistoryService.findAllByUserId(pageable, userId)
        );
    }
}
