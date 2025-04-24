let stompClient = null;

async function connect() {
  const socket = new SockJS("/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, async function (frame) {
    console.log("Connected: " + frame);

    // 🔄 取得目前登入會員的 ID（從 session 拿）
    const res = await fetch("/api/chat/session-id");
    if (!res.ok) {
      console.warn("❌ 無法取得 memberId，可能尚未登入");
      return;
    }

    const memberId = await res.text();
    if (!memberId) return;

    // ✅ 訂閱此會員的專屬頻道
    stompClient.subscribe(
      "/topic/private/" + memberId,
      function (messageOutput) {
        const message = JSON.parse(messageOutput.body);
        showMessage(
          `[${message.timestamp}] ${message.sender}：${message.content}`
        );
      }
    );
  });
}

async function loadChatHistory() {
  try {
    const res = await fetch("/api/chat/history/session");
    if (!res.ok) {
      console.warn("⚠️ 無法取得聊天紀錄，可能尚未登入");
      return;
    }
    const history = await res.json();
    history.forEach((msg) => {
      showMessage(`[${msg.timestamp}] ${msg.sender}：${msg.content}`);
    });
  } catch (err) {
    console.error("載入聊天紀錄時發生錯誤：", err);
  }
}

function sendMessage() {
  const from = document.getElementById("from").value;
  const content = document.getElementById("message").value;

  if (!from || !content) {
    alert("請輸入名稱與訊息！");
    return;
  }

  stompClient.send(
    "/app/chat",
    {},
    JSON.stringify({
      sender: from,
      content: content,
      // memberId 不要傳，從 session 自動取得
    })
  );

  document.getElementById("message").value = "";
}

function showMessage(message) {
  const chatBox = document.getElementById("chat-box");
  if (!chatBox) {
    console.warn("⚠️ chat-box 元素不存在");
    return;
  }

  chatBox.innerHTML += `<div>${message}</div>`;
  setTimeout(() => {
    chatBox.scrollTop = chatBox.scrollHeight;
  }, 0);
}

connect();
loadChatHistory();
