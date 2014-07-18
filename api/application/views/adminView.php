<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>Clinicas Admin</title>
	

  <link type="text/css" rel="stylesheet" href="<?php echo site_url('../css/bootstrap.min.css'); ?>" />	

  <?php          	
foreach($css_files as $file): ?>
	<link type="text/css" rel="stylesheet" href="<?php echo $file; ?>" />
<?php endforeach; ?>
<?php foreach($js_files as $file): ?>
	<script src="<?php echo $file; ?>"></script>
<?php endforeach; ?> 
	<style type="text/css">

	::selection{ background-color: #E13300; color: white; }
	::moz-selection{ background-color: #E13300; color: white; }
	::webkit-selection{ background-color: #E13300; color: white; }
	/*.span12{width: 90%}*/
	body {
		background-color: #fff;
		font: 13px/20px normal Helvetica, Arial, sans-serif;
		color: #4F5155;
	}

	a {
		color: #003399;
		background-color: transparent;
		font-weight: normal;
	}

	h1 {
		color: #444;
		background-color: transparent;
		border-bottom: 1px solid #ffffff;
		font-size: 19px;
		font-weight: normal;
		margin: 0 0 14px 0;
		padding: 14px 15px 10px 15px;
	}

	code {
		font-family: Consolas, Monaco, Courier New, Courier, monospace;
		font-size: 12px;
		background-color: #f9f9f9;
		border: 1px solid #D0D0D0;
		color: #002166;
		display: block;
		margin: 14px 0 14px 0;
		padding: 12px 10px 12px 10px;
	}

	p.footer{
		text-align: right;
		font-size: 11px;
		border-top: 1px solid #D0D0D0;
		line-height: 32px;
		padding: 0 10px 0 10px;
		margin: 20px 0 0 0;
	}
	
	.span12 {
		margin-left: 0px;
	}
	#container{
		margin: 10px;
		border: 1px solid #D0D0D0;
		-webkit-box-shadow: 0 0 8px #D0D0D0;
	}
	</style>

</head>
<body>

<div id="container" style="margin-top:-10px">
<!-- Static navbar -->
<div class="navbar navbar-fixed-top" style="height: 40px">
  <div class="navbar-inner" style="height: 40px; ; padding-right: 10px; ; padding-left: 10px;">
<a class="brand" href="<?php echo site_url('admin/') ?>"> Clinicas Admin</a>  	
    <ul class="nav">
		  <li><a href='<?php echo site_url('admin/user')?>'>Users</a></li>
		  <li><a href='<?php echo site_url('admin/article')?>'>Articles</a></li>
		  <li><a href='<?php echo site_url('admin/categories')?>'>Categories</a></li>
    </ul>
  </div>
</div>
	<div id="body" style="margin-top: 40px">
	<div class="container">

          	<?php
          	 echo $output; 
          	?>
</div>     </div>       	
</div>

</body>
</html>
