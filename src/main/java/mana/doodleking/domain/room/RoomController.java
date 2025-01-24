package mana.doodleking.domain.room;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mana.doodleking.domain.room.dto.*;
import mana.doodleking.global.MessageSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/room")
@RestController
@Slf4j
public class RoomController {
    private final RoomService roomService;
    private final MessageSender messageSender;
    @GetMapping
    public List<RoomSimple> getRoomList() {
        return roomService.getRoomList();
    }

    @MessageMapping("/createRoom")
    public void sendMessage(@Header("userId") Long userId, @Valid CreateRoomReq postRoomReq) {
        try {
            RoomDetail createdRoom = roomService.createRoom(userId, postRoomReq);
            messageSender.send("/queue/user/" + userId, createdRoom);

            List<RoomSimple> roomList = getRoomList();
            messageSender.send("/topic/lobby", roomList);
        } catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }

    @MessageMapping("/updateRoom")
    public void updateRoom(@Header("userId") Long userId, @Valid UpdateRoomReq updateRoomReq) {
        try {
            RoomDetail updatedRoom = roomService.updateRoom(userId, updateRoomReq);
            messageSender.send("/topic/room/" + updateRoomReq.getId(), updatedRoom);

        } catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }

    @MessageMapping("/enterRoom")
    public void enterRoom(@Header("userId") Long userId, RoomIdDTO roomIdDTO) {
        try {
            RoomDetail enterRoom = roomService.enterRoom(userId, roomIdDTO);
            messageSender.send("/queue/user/" + userId, enterRoom);

            messageSender.send("/topic/room/" + roomIdDTO.getRoomId(), enterRoom);

            List<RoomSimple> roomList = roomService.getRoomList();
            messageSender.send("/topic/lobby", roomList);
        }
        catch (OptimisticLockException e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, "동시성 문제 발생: 다시 시도해주세요.");
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }

    @MessageMapping("/quitRoom")
    public void quitRoom(@Header("userId") Long userId, RoomIdDTO roomIdDTO) {
        try {
            RoomDetail quitRoom = roomService.quitRoom(userId, roomIdDTO);
            messageSender.send("/queue/user/" + userId, quitRoom);

            messageSender.send("/topic/room/" + roomIdDTO.getRoomId(), quitRoom);

            List<RoomSimple> roomList = roomService.getRoomList();
            messageSender.send("/topic/lobby", roomList);
        }
        catch (OptimisticLockException e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, "동시성 문제 발생: 다시 시도해주세요.");
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }
}
