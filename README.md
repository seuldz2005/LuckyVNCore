# LuckyVNCore

Core API cho các plugin trong LuckyVN Network.

## Yêu cầu

- Server Minecraft `1.20.4`.
- Java `17`.
- PlaceholderAPI là tùy chọn; cần khi dùng placeholder.

## Cài đặt

1. Build plugin bằng Maven.
2. Chép file JAR vào thư mục `plugins` của server.
3. Khởi động lại server.

## Build

```bash
mvn clean package
```

## Lệnh

| Lệnh | Mô tả | Quyền |
| --- | --- | --- |
| `/tb <tin nhắn>` | Thông báo toàn server và console. Hỗ trợ màu `&` và `/n` để xuống dòng. | `luckyvncore.tb` |
| `/lkchat <player> <tin nhắn>` | Gửi chat dưới danh tính người chơi đang online. | `luckyvncore.lkchat` |

Hai quyền mặc định dành cho OP.

## `LKUtils.dispatchCommand`

Dùng cú pháp `[tag] nội dung` để xử lý hành động theo `sender`.

| Tag | Chức năng |
| --- | --- |
| `[console] <lệnh>` | Console chạy lệnh. |
| `[player] <lệnh>` | Player chạy lệnh; hỗ trợ `%player%`. |
| `[message] <tin nhắn>` | Gửi tin nhắn cho sender. |
| `[broadcast] <tin nhắn>` | Gửi tin nhắn cho toàn server và console. |
| `[chat] <tin nhắn>` | Player chat với nội dung chỉ định. |

Hỗ trợ `{random=min-max}` và PlaceholderAPI khi khả dụng.

Xem [wiki `LKUtils.dispatchCommand`](wiki/LKUtils-dispatchCommand.md) để biết cú pháp, ví dụ và giới hạn hiện tại.
