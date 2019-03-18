<head>
    <script></script>
</head>
<body>
    <button id="pagar" onclick="pagar()">Onepay</button>


    <script>
        (function (o, n, e, p, a, y) {
            var s = n.createElement(p);
            s.type = "text/javascript";
            s.src = e;
            s.onload = s.onreadystatechange = function () {
                if (!o && (!s.readyState
                    || s.readyState === "loaded")) {
                    y();
                }
            };
            var t = n.getElementsByTagName("script")[0];
            p = t.parentNode;
            p.insertBefore(s, t);
        })(false, document, "https://unpkg.com/transbank-onepay-frontend-sdk@1/lib/merchant.onepay.min.js",
            "script",window, function () {
                console.log("Onepay JS library successfully loaded.");
            });

        function pagar() {
            Onepay.checkout({
                endpoint: './transaction-create',
                commerceLogo: 'https://tu-url.com/images/icons/logo-01.png',
                callbackUrl: './onepay-result',
                transactionDescription: 'Set de pelotas',
                onclose: function (status) {
                    console.log('el estado recibido es: ', status);
                }
            });
        }
    </script>
</body>
