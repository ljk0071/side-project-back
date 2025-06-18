window.onload = function () {
    const ui = SwaggerUIBundle({
        url: "./swagger-ui/openapi3.yaml",
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
    })
    window.ui = ui

    // 사이드바에 링크 추가
    const sidebar = document.createElement('div');
    sidebar.setAttribute("id", "sidebar")
    sidebar.innerHTML = `
            <h3>API Categories</h3>
            <ul>
                <li><a href="#operations-tag-시스템_코드">시스템 코드</a></li>
                <li><a href="#operations-tag-시스템_타입_코드">시스템 타입 코드</a></li>
                <li><a href="#operations-tag-주차장_실시간_정보">주차장 실시간 정보</a></li>
                <li><a href="#operations-tag-주차장_정보">주차장 정보</a></li>
            </ul>
    `;
    document.body.insertBefore(sidebar, document.body.firstChild);
}
