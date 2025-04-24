let stompClient = null;
let currentSubscription = null;
let selectedMemberId = null;

function subscribeToMemberChannel(memberId) {
  if (currentSubscription) {
    currentSubscription.unsubscribe();
  }

  currentSubscription = stompClient.subscribe(
    "/topic/private/" + memberId,
    function (msg) {
      const chat = JSON.parse(msg.body);
      const chatBox = document.getElementById("chat-box");
      chatBox.innerHTML += `<div>[${chat.timestamp}] <strong>${chat.sender}</strong>：${chat.content}</div>`;
      chatBox.scrollTop = chatBox.scrollHeight;
    }
  );
}

async function loadHistory(memberId) {
  selectedMemberId = memberId;
  const res = await fetch(`/api/chat/history/${memberId}`);
  const messages = await res.json();
  const chatBox = document.getElementById("chat-box");
  chatBox.innerHTML = "";
  messages.forEach((msg) => {
    chatBox.innerHTML += `<div>[${msg.timestamp}] <strong>${msg.sender}</strong>：${msg.content}</div>`;
  });
  if (stompClient && stompClient.connected) {
    subscribeToMemberChannel(memberId);
  }
}

function connect() {
  if (stompClient && stompClient.connected) return;

  const socket = new SockJS("/ws");
  stompClient = Stomp.over(socket);
  stompClient.debug = null;

  stompClient.connect(
    {},
    () => {
      if (selectedMemberId) {
        subscribeToMemberChannel(selectedMemberId);
      }

      stompClient.subscribe("/topic/messages", (msg) => {
        const chat = JSON.parse(msg.body);
        const chatBox = document.getElementById("chat-box");
        chatBox.innerHTML += `<div>[${chat.timestamp}] <strong>${chat.sender}</strong>：${chat.content}</div>`;
        chatBox.scrollTop = chatBox.scrollHeight;
      });
    },
    () => {
      console.warn("WebSocket 斷線，3秒後自動重連...");
      setTimeout(connect, 3000);
    }
  );
}

async function loadMemberOptions() {
  const res = await fetch("/api/chat/members");
  const members = await res.json();
  const selector = document.getElementById("memberSelector");
  selector.innerHTML = `<option value="">請選擇會員</option>`; // 清空原本選項

  members.forEach((m) => {
    const option = document.createElement("option");
    option.value = m.memberId;
    option.textContent = `${m.name} (ID: ${m.memberId})`;
    selector.appendChild(option);
  });
}

window.sendMessage = function () {
  const selectedMemberId = document.getElementById("memberSelector").value;
  const from = document.getElementById("from").value;
  const content = document.getElementById("message").value;
  if (!from || !content) return alert("請輸入名稱與訊息！");
  if (!stompClient || !stompClient.connected) {
    alert("連線尚未完成，請稍候再試");
    return;
  }
  stompClient.send(
    "/app/chat",
    {},
    JSON.stringify({
      sender: "客服",
      content: content,
      memberId: selectedMemberId,
    })
  );
  document.getElementById("message").value = "";
};

connect();
