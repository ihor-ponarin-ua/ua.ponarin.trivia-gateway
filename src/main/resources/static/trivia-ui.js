const wsHandshakeEndpoint = '/ws/trivia';
const wsUserTopic = '/ws/topic/players';

$(function () {
    const jwtTokenAsString = loadJwtToken();
    const jwtToken = jwtTokenAsString ? readJwtToken(jwtTokenAsString) : null;

    if (jwtToken && !isJwtTokenExpired(jwtToken)) {
        showGameView();
        connect();
    } else {
        showLoginView();
    }
    assignActions();
});

function loadJwtToken() {
    return window.sessionStorage.getItem('triviaJwtToken');
}

function storeJwtToken(token) {
    window.sessionStorage.setItem('triviaJwtToken', token);
}

function readJwtToken(token) {
    return JSON.parse(atob(token.split('.')[1]));
}

function isJwtTokenExpired(token) {
    return Date.now() >= token.exp * 1000;
}

function showGameView() {
    $('#game_view').show();
    $('#login_view').hide();
}

function showLoginView() {
    $('#login_view').show();
    $('#game_view').hide();
}

function assignActions() {
    $('form').on('submit', function (e) {
        e.preventDefault();
    });

    $('#login').click(() => login())
}

function login() {
    const login = $('#inputLogin').val();
    const password = $('#inputPassword').val();
    console.log(login)
    axios.post('/api/v1/authenticator/token', {
        login,
        password
    }).then(function (response) {
            const token = response.data.token;
            storeJwtToken(token);
            showGameView();
            connect();
        })
        .catch(function (error) {
            console.log(error);
        });
}

function connect() {
    const jwtTokenAsString = loadJwtToken();
    const sockJs = new SockJS(wsHandshakeEndpoint + "?token=" + jwtTokenAsString);
    const stompClient = Stomp.over(sockJs);

    stompClient.connect({'token': jwtTokenAsString }, () => {
        stompClient.subscribe(wsUserTopic, event => playersListChanged(JSON.parse(event.body)))
    });
}

function playersListChanged(players) {
    const playersNames = players
        .filter(player => player.voteStatus === 'IN_PROGRESS')
        .map(player => `<li>${player.firstName} ${player.lastName}</li>`);
    $('#playersList').html(playersNames);
}
