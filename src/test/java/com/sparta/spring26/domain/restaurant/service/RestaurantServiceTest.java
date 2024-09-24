package com.sparta.spring26.domain.restaurant.service;

import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantUpdateDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseListDto;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.enums.RestaurantCategory;
import com.sparta.spring26.domain.restaurant.enums.RestaurantStatus;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.domain.user.enums.UserStatus;
import com.sparta.spring26.domain.user.repository.UserRepository;
import com.sparta.spring26.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Nested
    @DisplayName("가게 생성 테스트")
    class createRestaurantTest {
        @Test
        @DisplayName("가게 생성 성공 테스트")
        void createRestaurant_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner Name");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            RestaurantRequestDto requestDto = new RestaurantRequestDto();
            ReflectionTestUtils.setField(requestDto, "name", "New Restaurant");
            ReflectionTestUtils.setField(requestDto, "description", "New Description");
            ReflectionTestUtils.setField(requestDto, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(requestDto, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(requestDto, "minDeliveryPrice", 10000);
            ReflectionTestUtils.setField(requestDto, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(requestDto, "address", "New Address");

            Restaurant savedRestaurant = new Restaurant();
            ReflectionTestUtils.setField(savedRestaurant, "id", 1L);
            ReflectionTestUtils.setField(savedRestaurant, "name", requestDto.getName());
            ReflectionTestUtils.setField(savedRestaurant, "owner", owner);

            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
            given(restaurantRepository.countByOwnerIdAndStatus(anyLong(), any(RestaurantStatus.class))).willReturn(0L);
            given(restaurantRepository.save(any(Restaurant.class))).willReturn(savedRestaurant);

            // when
            RestaurantResponseDto responseDto = restaurantService.createRestaurant(requestDto, owner.getId());

            // then
            assertNotNull(responseDto);
            assertEquals(responseDto.getName(), responseDto.getName());
            verify(restaurantRepository).save(any(Restaurant.class));
        }

        @Test
        @DisplayName("가게 생성 실패 테스트 - 사용자가 존재하지 않음")
        void createRestaurant_UserNotFound() {
            // given
            RestaurantRequestDto requestDto = new RestaurantRequestDto();
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.createRestaurant(requestDto, 1L));
            assertEquals("해당 유저 정보를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가게 생성 실패 테스트 - 사용자 권한이 OWNER가 아님")
        void createRestaurant_NotOwner() {
            // given
            User notOwner = new User();
            ReflectionTestUtils.setField(notOwner, "id", 1L);
            ReflectionTestUtils.setField(notOwner, "role", UserRole.USER);

            RestaurantRequestDto requestDto = new RestaurantRequestDto();
            given(userRepository.findById(anyLong())).willReturn(Optional.of(notOwner));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.createRestaurant(requestDto, 1L));
            assertEquals("User is not an owner", exception.getMessage());
        }

        @Test
        @DisplayName("가게 생성 실패 테스트 - 최대 가게 수 초과")
        void createRestaurant_MaxRestaurantLimitExceeded() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);

            RestaurantRequestDto requestDto = new RestaurantRequestDto();
            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
            given(restaurantRepository.countByOwnerIdAndStatus(anyLong(), any(RestaurantStatus.class))).willReturn(3L);

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.createRestaurant(requestDto, 1L));
            assertEquals("Owner has reached the maximum limit of restaurants", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("가게 정보 수정 테스트")
    class updateRestaurantPartialTest {
        @Test
        @DisplayName("가게 정보 수정 성공 테스트")
        void updateRestaurant_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner Name");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "name", "Original Restaurant");
            ReflectionTestUtils.setField(restaurant, "description", "Original Description");
            ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
            ReflectionTestUtils.setField(restaurant, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant, "address", "Original Address");
            ReflectionTestUtils.setField(restaurant, "owner", owner);

            RestaurantUpdateDto updateDto = new RestaurantUpdateDto();
            ReflectionTestUtils.setField(updateDto, "name", "Updated Restaurant");
            ReflectionTestUtils.setField(updateDto, "description", "Updated Description");
            ReflectionTestUtils.setField(updateDto, "openTime", LocalTime.of(10, 0));
            ReflectionTestUtils.setField(updateDto, "closeTime", LocalTime.of(23, 0));
            ReflectionTestUtils.setField(updateDto, "minDeliveryPrice", 15000);
            ReflectionTestUtils.setField(updateDto, "category", RestaurantCategory.JAPANESE);
            ReflectionTestUtils.setField(updateDto, "address", "Updated Address");

            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            RestaurantResponseDto responseDto = restaurantService.updateRestaurantPartial(restaurant.getId(), updateDto, owner.getId());

            // then
            assertNotNull(responseDto);
            assertEquals(updateDto.getName(), responseDto.getName());
            assertEquals(updateDto.getDescription(), responseDto.getDescription());
            assertEquals(updateDto.getOpenTime(), responseDto.getOpenTime());
            assertEquals(updateDto.getCloseTime(), responseDto.getCloseTime());
            assertEquals(updateDto.getMinDeliveryPrice(), responseDto.getMinDeliveryPrice());
            assertEquals(updateDto.getCategory(), responseDto.getCategory());
            assertEquals(updateDto.getAddress(), responseDto.getAddress());
        }

        @Test
        @DisplayName("가게 정보 수정 실패 테스트 - 사용자가 존재하지 않음")
        void updateRestaurant_UserNotFound() {
            // given
            RestaurantUpdateDto updateDto = new RestaurantUpdateDto();
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.updateRestaurantPartial(1L, updateDto, 1L));
            assertEquals("해당 유저 정보를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가게 정보 수정 실패 테스트 - 가게가 존재하지 않음")
        void updateRestaurant_RestaurantNotFound() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);

            RestaurantUpdateDto updateDto = new RestaurantUpdateDto();
            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.updateRestaurantPartial(1L, updateDto, owner.getId()));
            assertEquals("해당 식당 정보를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가게 정보 수정 실패 테스트 - 권한 없음")
        void updateRestaurant_NotOwner() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);

            User notOwner = new User();
            ReflectionTestUtils.setField(notOwner, "id", 2L);
            ReflectionTestUtils.setField(notOwner, "role", UserRole.OWNER);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "owner", owner);

            RestaurantUpdateDto updateDto = new RestaurantUpdateDto();
            given(userRepository.findById(anyLong())).willReturn(Optional.of(notOwner));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.updateRestaurantPartial(restaurant.getId(), updateDto, notOwner.getId()));
            assertEquals("User is not the owner of this restaurant", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("가게 단건 조회 테스트")
    class GetRestaurantTest {
        @Test
        @DisplayName("가게 단건 조회 성공 테스트")
        void getRestaurant_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner Name");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "name", "Test Restaurant");
            ReflectionTestUtils.setField(restaurant, "description", "Test Description");
            ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
            ReflectionTestUtils.setField(restaurant, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant, "address", "Test Address");
            ReflectionTestUtils.setField(restaurant, "owner", owner);
            ReflectionTestUtils.setField(restaurant, "status", RestaurantStatus.OPEN);

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            RestaurantResponseDto response = restaurantService.getRestaurant(restaurant.getId());

            // then
            assertNotNull(response);
            assertEquals(restaurant.getId(), response.getId());
            assertEquals(restaurant.getName(), response.getName());
            assertEquals(restaurant.getDescription(), response.getDescription());
            assertEquals(restaurant.getOpenTime(), response.getOpenTime());
            assertEquals(restaurant.getCloseTime(), response.getCloseTime());
            assertEquals(restaurant.getMinDeliveryPrice(), response.getMinDeliveryPrice());
            assertEquals(restaurant.getCategory(), response.getCategory());
            assertEquals(restaurant.getAddress(), response.getAddress());
        }

        @Test
        @DisplayName("가게 단건 조회 실패 테스트 - 가게가 존재하지 않음")
        void getRestaurant_RestaurantNotFound() {
            // given
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.getRestaurant(1L));
            assertEquals("해당 식당 정보를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가게 단건 조회 실패 테스트 - 폐업한 가게")
        void getRestaurant_ClosedRestaurant() {
            // given
            Restaurant closedRestaurant = new Restaurant();
            ReflectionTestUtils.setField(closedRestaurant, "id", 1L);
            ReflectionTestUtils.setField(closedRestaurant, "status", RestaurantStatus.CLOSED);

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(closedRestaurant));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.getRestaurant(1L));
            assertEquals("해당 식당은 폐업했습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("가게 목록 조회 테스트")
    class GetRestaurantListTest {
        @Test
        @DisplayName("가게 목록 조회 성공 테스트 - 모든 가게")
        void getRestaurantList_AllRestaurants_Success() {
            // given
            User owner1 = new User();
            ReflectionTestUtils.setField(owner1, "id", 1L);
            ReflectionTestUtils.setField(owner1, "email", "owner1@test.com");
            ReflectionTestUtils.setField(owner1, "name", "Owner 1");
            ReflectionTestUtils.setField(owner1, "phone", "01012345678");
            ReflectionTestUtils.setField(owner1, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner1, "status", UserStatus.ACTIVE);

            User owner2 = new User();
            ReflectionTestUtils.setField(owner2, "id", 2L);
            ReflectionTestUtils.setField(owner2, "email", "owner2@test.com");
            ReflectionTestUtils.setField(owner2, "name", "Owner 2");
            ReflectionTestUtils.setField(owner2, "phone", "01087654321");
            ReflectionTestUtils.setField(owner2, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner2, "status", UserStatus.ACTIVE);

            Restaurant restaurant1 = new Restaurant();
            ReflectionTestUtils.setField(restaurant1, "id", 1L);
            ReflectionTestUtils.setField(restaurant1, "name", "Restaurant 1");
            ReflectionTestUtils.setField(restaurant1, "description", "Korean Restaurant");
            ReflectionTestUtils.setField(restaurant1, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(restaurant1, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(restaurant1, "minDeliveryPrice", 10000);
            ReflectionTestUtils.setField(restaurant1, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant1, "address", "Seoul, Korea");
            ReflectionTestUtils.setField(restaurant1, "owner", owner1);
            ReflectionTestUtils.setField(restaurant1, "status", RestaurantStatus.OPEN);

            Restaurant restaurant2 = new Restaurant();
            ReflectionTestUtils.setField(restaurant2, "id", 2L);
            ReflectionTestUtils.setField(restaurant2, "name", "Restaurant 2");
            ReflectionTestUtils.setField(restaurant2, "description", "Japanese Restaurant");
            ReflectionTestUtils.setField(restaurant2, "openTime", LocalTime.of(11, 0));
            ReflectionTestUtils.setField(restaurant2, "closeTime", LocalTime.of(23, 0));
            ReflectionTestUtils.setField(restaurant2, "minDeliveryPrice", 15000);
            ReflectionTestUtils.setField(restaurant2, "category", RestaurantCategory.JAPANESE);
            ReflectionTestUtils.setField(restaurant2, "address", "Busan, Korea");
            ReflectionTestUtils.setField(restaurant2, "owner", owner2);
            ReflectionTestUtils.setField(restaurant2, "status", RestaurantStatus.OPEN);

            List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
            Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);

            Pageable pageable = PageRequest.of(0, 10);
            given(restaurantRepository.findAllByStatus(eq(RestaurantStatus.OPEN), any(Pageable.class))).willReturn(restaurantPage);

            // when
            Page<RestaurantResponseListDto> response = restaurantService.getRestaurantList(null, null, pageable);

            // then
            assertNotNull(response);
            assertEquals(2, response.getContent().size());
            assertEquals(restaurant1.getName(), response.getContent().get(0).getName());
            assertEquals(restaurant2.getName(), response.getContent().get(1).getName());
        }

        @Test
        @DisplayName("가게 목록 조회 성공 테스트 - 이름으로 검색")
        void getRestaurantList_SearchByName_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "name", "Korean Restaurant");
            ReflectionTestUtils.setField(restaurant, "description", "Best Korean food");
            ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
            ReflectionTestUtils.setField(restaurant, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant, "address", "Seoul, Korea");
            ReflectionTestUtils.setField(restaurant, "owner", owner);
            ReflectionTestUtils.setField(restaurant, "status", RestaurantStatus.OPEN);

            List<Restaurant> restaurants = List.of(restaurant);
            Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);

            Pageable pageable = PageRequest.of(0, 10);
            given(restaurantRepository.findByNameContainingIgnoreCaseAndStatus(anyString(), eq(RestaurantStatus.OPEN), any(Pageable.class)))
                    .willReturn(restaurantPage);

            // when
            Page<RestaurantResponseListDto> response = restaurantService.getRestaurantList("Korea", null, pageable);

            // then
            assertNotNull(response);
            assertEquals(1, response.getContent().size());
            assertEquals(restaurant.getName(), response.getContent().get(0).getName());
            assertEquals(restaurant.getMinDeliveryPrice(), response.getContent().get(0).getMinDeliveryPrice());
            verify(restaurantRepository).findByNameContainingIgnoreCaseAndStatus(eq("Korea"), eq(RestaurantStatus.OPEN), eq(pageable));
        }

        @Test
        @DisplayName("카테고리로 검색 성공 테스트")
        void getRestaurantList_SearchByCategory_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "name", "Sushi Restaurant");
            ReflectionTestUtils.setField(restaurant, "description", "Best Japanese food");
            ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(11, 0));
            ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 15000);
            ReflectionTestUtils.setField(restaurant, "category", RestaurantCategory.JAPANESE);
            ReflectionTestUtils.setField(restaurant, "address", "Tokyo, Japan");
            ReflectionTestUtils.setField(restaurant, "owner", owner);
            ReflectionTestUtils.setField(restaurant, "status", RestaurantStatus.OPEN);
            List<Restaurant> restaurants = List.of(restaurant);
            Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);

            Pageable pageable = PageRequest.of(0, 10);
            given(restaurantRepository.findByCategoryAndStatus(eq(RestaurantCategory.JAPANESE), eq(RestaurantStatus.OPEN), any(Pageable.class)))
                    .willReturn(restaurantPage);

            // when
            Page<RestaurantResponseListDto> response = restaurantService.getRestaurantList(null, RestaurantCategory.JAPANESE, pageable);

            // then
            assertNotNull(response);
            assertEquals(1, response.getContent().size());
            RestaurantResponseListDto dto = response.getContent().get(0);
            assertEquals(restaurant.getId(), dto.getId());
            assertEquals(restaurant.getName(), dto.getName());

            verify(restaurantRepository).findByCategoryAndStatus(eq(RestaurantCategory.JAPANESE), eq(RestaurantStatus.OPEN), eq(pageable));
        }

        @Test
        @DisplayName("가게 목록 조회 성공 테스트 - 이름과 카테고리로 동시 검색")
        void getRestaurantList_SearchByNameAndCategory_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "name", "Korean BBQ");
            ReflectionTestUtils.setField(restaurant, "description", "Best Korean BBQ");
            ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(17, 0));
            ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(2, 0));
            ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 20000);
            ReflectionTestUtils.setField(restaurant, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant, "address", "Seoul, Korea");
            ReflectionTestUtils.setField(restaurant, "owner", owner);
            ReflectionTestUtils.setField(restaurant, "status", RestaurantStatus.OPEN);

            List<Restaurant> restaurants = List.of(restaurant);
            Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);

            Pageable pageable = PageRequest.of(0, 10);
            given(restaurantRepository.findByNameContainingIgnoreCaseAndCategoryAndStatus(anyString(), eq(RestaurantCategory.KOREAN), eq(RestaurantStatus.OPEN), any(Pageable.class)))
                    .willReturn(restaurantPage);

            // when
            Page<RestaurantResponseListDto> response = restaurantService.getRestaurantList("Korean", RestaurantCategory.KOREAN, pageable);

            // then
            assertNotNull(response);
            assertEquals(1, response.getContent().size());
            assertEquals(restaurant.getName(), response.getContent().get(0).getName());

            verify(restaurantRepository).findByNameContainingIgnoreCaseAndCategoryAndStatus(eq("Korean"), eq(RestaurantCategory.KOREAN), eq(RestaurantStatus.OPEN), eq(pageable));
        }

        @Test
        @DisplayName("가게 목록 조회 테스트 - 검색 결과가 없는 경우")
        void getRestaurantList_NoResults() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Restaurant> emptyPage = new PageImpl<>(Collections.emptyList());

            given(restaurantRepository.findByNameContainingIgnoreCaseAndStatus(anyString(), eq(RestaurantStatus.OPEN), any(Pageable.class)))
                    .willReturn(emptyPage);

            // when
            Page<RestaurantResponseListDto> response = restaurantService.getRestaurantList("NonExistent", null, pageable);

            // then
            assertNotNull(response);
            assertTrue(response.getContent().isEmpty());
            assertEquals(0, response.getTotalElements());

            verify(restaurantRepository).findByNameContainingIgnoreCaseAndStatus(eq("NonExistent"), eq(RestaurantStatus.OPEN), eq(pageable));
        }

        @Test
        @DisplayName("가게 목록 조회 테스트 - 페이징 처리")
        void getRestaurantList_Pagination() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant1 = new Restaurant();
            ReflectionTestUtils.setField(restaurant1, "id", 1L);
            ReflectionTestUtils.setField(restaurant1, "name", "Restaurant 1");
            ReflectionTestUtils.setField(restaurant1, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant1, "owner", owner);
            ReflectionTestUtils.setField(restaurant1, "status", RestaurantStatus.OPEN);

            Restaurant restaurant2 = new Restaurant();
            ReflectionTestUtils.setField(restaurant2, "id", 2L);
            ReflectionTestUtils.setField(restaurant2, "name", "Restaurant 2");
            ReflectionTestUtils.setField(restaurant2, "category", RestaurantCategory.JAPANESE);
            ReflectionTestUtils.setField(restaurant2, "owner", owner);
            ReflectionTestUtils.setField(restaurant2, "status", RestaurantStatus.OPEN);

            List<Restaurant> restaurants = List.of(restaurant1, restaurant2);
            Pageable pageable = PageRequest.of(0, 2);
            Page<Restaurant> restaurantPage = new PageImpl<>(restaurants, pageable, 5); // 총 5개의 결과가 있다고 가정

            given(restaurantRepository.findAllByStatus(eq(RestaurantStatus.OPEN), any(Pageable.class)))
                    .willReturn(restaurantPage);

            // when
            Page<RestaurantResponseListDto> response = restaurantService.getRestaurantList(null, null, pageable);

            // then
            assertNotNull(response);
            assertEquals(2, response.getContent().size());
            assertEquals(5, response.getTotalElements());
            assertEquals(3, response.getTotalPages());
            assertTrue(response.isFirst());
            assertFalse(response.isLast());

            verify(restaurantRepository).findAllByStatus(eq(RestaurantStatus.OPEN), eq(pageable));
        }
    }

    @Nested
    @DisplayName("가게 폐업 성공 테스트")
    class closeRestaurantTest {
        @Test
        @DisplayName("가게 폐업 성공 테스트")
        void closeRestaurant_Success() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "email", "owner@test.com");
            ReflectionTestUtils.setField(owner, "name", "Owner");
            ReflectionTestUtils.setField(owner, "phone", "01012345678");
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "status", UserStatus.ACTIVE);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "name", "Test Restaurant");
            ReflectionTestUtils.setField(restaurant, "description", "Test Description");
            ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
            ReflectionTestUtils.setField(restaurant, "category", RestaurantCategory.KOREAN);
            ReflectionTestUtils.setField(restaurant, "address", "Test Address");
            ReflectionTestUtils.setField(restaurant, "owner", owner);
            ReflectionTestUtils.setField(restaurant, "status", RestaurantStatus.OPEN);

            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            restaurantService.closeRestaurant(restaurant.getId(), owner.getId());

            // then
            assertEquals(RestaurantStatus.CLOSED, restaurant.getStatus());
            verify(restaurantRepository).findById(eq(restaurant.getId()));
            verify(userRepository).findById(eq(owner.getId()));
        }

        @Test
        @DisplayName("가게 폐업 실패 테스트 - 사용자가 존재하지 않음")
        void closeRestaurant_UserNotFound() {
            // given
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.closeRestaurant(1L, 1L));
            assertEquals("해당 유저 정보를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가게 폐업 실패 테스트 - 가게가 존재하지 않음")
        void closeRestaurant_RestaurantNotFound() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);

            given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.closeRestaurant(1L, owner.getId()));
            assertEquals("해당 식당 정보를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가게 폐업 실패 테스트 - 가게 소유자가 아님")
        void closeRestaurant_NotRestaurantOwner() {
            // given
            User owner = new User();
            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);

            User notOwner = new User();
            ReflectionTestUtils.setField(notOwner, "id", 2L);
            ReflectionTestUtils.setField(notOwner, "role", UserRole.OWNER);

            Restaurant restaurant = new Restaurant();
            ReflectionTestUtils.setField(restaurant, "id", 1L);
            ReflectionTestUtils.setField(restaurant, "owner", owner);
            ReflectionTestUtils.setField(restaurant, "status", RestaurantStatus.OPEN);

            given(userRepository.findById(eq(notOwner.getId()))).willReturn(Optional.of(notOwner));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> restaurantService.closeRestaurant(restaurant.getId(), notOwner.getId()));
            assertEquals("User is not the owner of this restaurant", exception.getMessage());
        }
    }
}