upstream Next {
	# server localhost:3000;
	server 192.168.0.9:3000;
}

upstream Api {
	server localhost:8780;
}


# upstream Kuber {
# 	server localhost:8888;
# }

server {
    listen	80;
    listen	443 ssl http2;

    if ($scheme = 'http') {
        return 301 https://$host$request_uri;
    }

    server_name nmedalist.ru;
    ssl_certificate /etc/ssl/nmedalist.crt;
    ssl_certificate_key /etc/ssl/nmedalist.key;

	ssl_session_cache shared:SSL:10m;
	ssl_session_timeout 10m;
	keepalive_timeout 70;

	ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3; # Dropping SSLv3, ref: POODLE
	ssl_prefer_server_ciphers on;
	ssl_ciphers ECDH+AESGCM:ECDH+AES256:ECDH+AES128:DH+3DES:!ADH:!AECDH:!MD5;

	ssl_stapling on;
	ssl_trusted_certificate /etc/ssl/ca.crt;
	resolver 8.8.8.8;

	# location / {
	# 	proxy_pass $scheme://localhost:8888/;
	# 	proxy_redirect     off;
	# 	proxy_set_header   Host             $host;
	# 	proxy_set_header   X-Real-IP        $remote_addr;
	# 	proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
	# }   


	# location / {
    # 	# Reverse proxy for Spring server
    # 	proxy_pass http://Next;
	# 	# proxy_redirect     off;
    # 	proxy_http_version 1.1;
    # 	proxy_set_header Upgrade $http_upgrade;
    # 	proxy_set_header Connection "upgrade";
    # 	proxy_set_header Host $host/;
    # 	proxy_set_header X-Real-IP $remote_addr;
    # 	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    # 	proxy_set_header X-Forwarded-Proto $scheme;
    # 	proxy_set_header X-Forwarded-Host $host;
    # 	proxy_set_header X-Forwarded-Port $server_port;

    # 	# we need to remove this 404 handling
    # 	# because of Next's error handling and _next folder
    # 	# try_files $uri $uri/ =404;	
	# }


	# location /docker {
    # 	# Reverse proxy for Spring server
    # 	proxy_pass http://Docker;
    # 	proxy_http_version 1.1;
    # 	proxy_set_header Upgrade $http_upgrade;
    # 	proxy_set_header Connection "upgrade";
    # 	proxy_set_header Host $host;
    # 	proxy_set_header X-Real-IP $remote_addr;
    # 	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    # 	proxy_set_header X-Forwarded-Proto $scheme;
    # 	proxy_set_header X-Forwarded-Host $host;
    # 	proxy_set_header X-Forwarded-Port $server_port;

    # 	# we need to remove this 404 handling
    # 	# because of Next's error handling and _next folder
    # 	# try_files $uri $uri/ =404;	
	# }

    location /api/v1 {
    	# Reverse proxy for Spring server
    	proxy_pass http://Api;
    	proxy_http_version 1.1;
    	proxy_set_header Upgrade $http_upgrade;
    	proxy_set_header Connection "upgrade";
    	proxy_set_header Host $host;
    	proxy_set_header X-Real-IP $remote_addr;
    	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    	proxy_set_header X-Forwarded-Proto $scheme;
    	proxy_set_header X-Forwarded-Host $host;
    	proxy_set_header X-Forwarded-Port $server_port;

    	# we need to remove this 404 handling
    	# because of Next's error handling and _next folder
    	# try_files $uri $uri/ =404;	
	}

	location / {
    	# Reverse proxy for Next server
    	proxy_pass http://Next;
    	proxy_http_version 1.1;
    	proxy_set_header Upgrade $http_upgrade;
    	proxy_set_header Connection "upgrade";
    	proxy_set_header Host $host;
    	proxy_set_header X-Real-IP $remote_addr;
    	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    	proxy_set_header X-Forwarded-Proto $scheme;
    	proxy_set_header X-Forwarded-Host $host;
    	proxy_set_header X-Forwarded-Port $server_port;

    	# we need to remove this 404 handling
    	# because of Next's error handling and _next folder
    	# try_files $uri $uri/ =404;	
	}



}
