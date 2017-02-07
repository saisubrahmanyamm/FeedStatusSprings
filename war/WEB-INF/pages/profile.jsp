<%response.setHeader("Cache-control","no-store");
response.setHeader("Pragma","no-cache");
response.setDateHeader("Expires", -1); %>
<html>
<head>
<title>Profile</title>
<link type="text/css" rel="stylesheet" href="/images/samplecss.css" />

</head>
<body>
	<div class="heading">
		<b>Feed Status</b>

		<p style="font-family: cursive; font-size: 20px; text-align: left">

			<a href="home">Home</a>
			<button id="signup-button"
				style="padding: 5.2px 7px; background-color: #B3008E; a: hover: none">My
				Profile</button>
			<a href="logout">Log out </a>
		</p>
	</div>
	<div>
		<p style="text-align: left; font-family: cursive; font-size: 22px; color: #3e74a0; margin-left: 1cm">Hello,
			This is your profile and this is your image.</p>
	</div>
	<div style="margin: 2cm 0cm 0cm 3cm">
		<img alt="userpicture" src="/images/userpicture.jpg" width="300px"
			height="300px">
	</div>
</body>
</html>