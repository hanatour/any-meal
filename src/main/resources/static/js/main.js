// 위치 조회와 식당 추천 결과 표시를 처리하는 공통 스크립트
(function () {
    var isKorean = document.documentElement.lang === 'ko';
    var messages = isKorean ? {
        requestFailed: '요청 오류: ',
        noRestaurant: '추천할 식당이 없어요.',
        invalidResponse: '응답을 처리할 수 없어요.',
        lead: '오늘 <span class="text-content-bold">식사</span>는',
        followUp: '어때요?',
        defaultLocation: '위치를 사용할 수 없어 기본 지역으로 추천합니다.'
    } : {
        requestFailed: 'Request failed: ',
        noRestaurant: 'No nearby restaurant found.',
        invalidResponse: 'We could not process the response.',
        lead: 'How about',
        followUp: '<span class="text-content-small">for your</span> <span class="text-content-bold">meal</span> <span class="text-content-small">today?</span>',
        defaultLocation: 'Location is unavailable, so we used the default area.'
    };
    var usedDefaultLocation = false;
    var positionOptions = {
        enableHighAccuracy: true,
        timeout: 5000,
        maximumAge: 300000
    };

    window.addEventListener('load', geoFindMe);

    function geoFindMe() {
        if (!navigator.geolocation) {
            alert('Geolocation is not supported by your browser');
        } else {
            navigator.geolocation.getCurrentPosition(geoFindMeSuccess, geoFindMeError, positionOptions);
        }
    }

    function geoFindMeSuccess(position) {
        usedDefaultLocation = false;
        getFunc(position.coords.longitude, position.coords.latitude);
    }

    function geoFindMeError(err) {
        console.log(new Date().toUTCString(), 'Unable to retrieve your location', err);
        usedDefaultLocation = true;
        getFunc('', '');
    }

    function getFunc(x, y) {
        var url = '/restaurant/near?x=' + encodeURIComponent(x) + '&y=' + encodeURIComponent(y);
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState !== 4) return;
            if (xhr.status === 200 || xhr.status === 204) {
                process(xhr.status, xhr.responseText);
            } else {
                document.getElementById('result').innerHTML =
                    '<div class="text-content">' + messages.requestFailed + xhr.status + '</div>';
            }
        };
        xhr.send();
    }

    function process(status, responseText) {
        var resultEl = document.getElementById('result');
        if (status === 204 || !responseText || responseText.trim() === '') {
            resultEl.innerHTML = '<div class="vertical-align-content"><div class="text-content">' + messages.noRestaurant + '</div></div>';
            return;
        }
        var json;
        try {
            json = JSON.parse(responseText);
        } catch (e) {
            resultEl.innerHTML = '<div class="text-content">' + messages.invalidResponse + '</div>';
            return;
        }
        var placeName = json && json.place_name;
        if (!placeName) {
            resultEl.innerHTML = '<div class="vertical-align-content"><div class="text-content">' + messages.noRestaurant + '</div></div>';
            return;
        }
        var html = '<div class="vertical-align-content">' +
            '<div class="text-content">' + messages.lead + '</div>' +
            '<div class="text-content"><a href="' + safePlaceUrl(json.place_url) + '">&quot;' + escapeHtml(placeName) + '&quot;</a></div>' +
            '<div class="text-content">' + messages.followUp + '</div>' +
            (usedDefaultLocation ? '<div class="text-content location-fallback">' + messages.defaultLocation + '</div>' : '') +
            '</div>';
        resultEl.innerHTML = html;
        showMapIfAvailable(resultEl, json);
    }

    function escapeHtml(value) {
        return String(value).replace(/[&<>"']/g, function (char) {
            return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[char];
        });
    }

    function safePlaceUrl(value) {
        try {
            var url = new URL(value, window.location.origin);
            return url.protocol === 'http:' || url.protocol === 'https:' ? url.href : '#';
        } catch (e) {
            return '#';
        }
    }

    function showMapIfAvailable(container, json) {
        var lat = Number.parseFloat(json && json.y);
        var lng = Number.parseFloat(json && json.x);
        if (Number.isNaN(lat) || Number.isNaN(lng)) return;
        var req = new XMLHttpRequest();
        req.open('GET', '/api/config', true);
        req.onreadystatechange = function () {
            if (req.readyState !== 4) return;
            try {
                var config = req.status === 200 ? JSON.parse(req.responseText) : {};
                var clientId = config.naverMapClientId || '';
                if (!clientId) return;
                var mapDiv = document.createElement('div');
                mapDiv.id = 'map';
                container.appendChild(mapDiv);
                var script = document.createElement('script');
                script.src = 'https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=' + encodeURIComponent(clientId);
                script.onload = function () {
                    var center = new naver.maps.LatLng(lat, lng);
                    var map = new naver.maps.Map('map', { center: center, zoom: 16 });
                    new naver.maps.Marker({ position: center, map: map });
                };
                document.head.appendChild(script);
            } catch (e) { console.warn('Naver map load failed', e); }
        };
        req.send();
    }
})();
