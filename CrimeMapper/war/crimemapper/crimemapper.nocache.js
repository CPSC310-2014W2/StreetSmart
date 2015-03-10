<<<<<<< HEAD
/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * This startup script is used when we run superdevmode from an app server.
 *
 * The main goal is to avoid installing bookmarklets for host:port/module
 * to load and recompile the application.
 */
(function($wnd, $doc){
  // Don't support browsers without session storage: IE6/7
  var badBrowser = 'Unable to load Super Dev Mode of "crimemapper" because\n';
  if (!('sessionStorage' in $wnd)) {
    $wnd.alert(badBrowser +  'this browser does not support "sessionStorage".');
    return;
  }

  //We don't import properties.js so we have to update active modules here
  $wnd.__gwt_activeModules = $wnd.__gwt_activeModules || {};
  $wnd.__gwt_activeModules['crimemapper'] = {
    'moduleName' : 'crimemapper',
    'bindings' : function() {
      return {};
    }
  };

  // Reuse compute script base
  /*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * A simplified version of computeScriptBase.js that's used only when running
 * in Super Dev Mode. (We don't want the default version because it allows the
 * web page to override it using a meta tag.)
 *
 * Prerequisite: we assume that the first script tag using a URL ending with
 * "/crimemapper.nocache.js" is the one that loaded us. Normally this happens
 * because DevModeRedirectHook.js loaded this nocache.js script by prepending a
 * script tag with an absolute URL to head. (However, it's also okay for an html
 * file included in the GWT compiler's output to load the nocache.js file using
 * a relative URL.)
 */
function computeScriptBase() {
  // TODO(skybrian) This approach won't work for workers.

  $wnd.__gwt_activeModules['crimemapper'].superdevmode = true;

  var expectedSuffix = '/crimemapper.nocache.js';

  var scriptTags = $doc.getElementsByTagName('script');
  for (var i = 0;; i++) {
    var tag = scriptTags[i];
    if (!tag) {
      break;
    }
    var candidate = tag.src;
    var lastMatch = candidate.lastIndexOf(expectedSuffix);
    if (lastMatch == candidate.length - expectedSuffix.length) {
      // Assumes that either the URL is absolute, or it's relative
      // and the html file is hosted by this code server.
      return candidate.substring(0, lastMatch + 1);
    }
  }

  $wnd.alert('Unable to load Super Dev Mode version of ' + crimemapper + ".");
}
;

  // document.head does not exist in IE8
  var $head = $doc.head || $doc.getElementsByTagName('head')[0];

  // Quick way to compute the user.agent, it works almost the same than
  // UserAgentPropertyGenerator, but we cannot reuse it without depending
  // on gwt-user.jar.
  // This reduces compilation time since we only compile for one ua.
  var ua = $wnd.navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode || 0;
  ua = /webkit/.test(ua)? 'safari' : /gecko/.test(ua) || docMode > 10 ? 'gecko1_8' :
       /msie/.test(ua) && docMode > 7 ? 'ie' + docMode : '';
  if (!ua && docMode) {
    $wnd.alert(badBrowser +  'your browser is running "Compatibility View" for IE' + docMode + '.');
    return;
  }

  // We use a different key for each module so that we can turn on dev mode
  // independently for each.
  var devModeHookKey = '__gwtDevModeHook:crimemapper';
  var devModeSessionKey = '__gwtDevModeSession:crimemapper';

  // Compute some codeserver urls so as the user does not need bookmarklets
  var hostName = $wnd.location.hostname;
  var serverUrl = 'http://' + hostName + ':9876';
  var nocacheUrl = serverUrl + '/crimemapper/crimemapper.nocache.js';

  // Save supder-devmode url in session
  $wnd.sessionStorage[devModeHookKey] = nocacheUrl;
  // Save user.agent in session
  $wnd.sessionStorage[devModeSessionKey] = 'user.agent=' + ua + '&';

  // Set bookmarklet params in window
  $wnd.__gwt_bookmarklet_params = {'server_url': serverUrl};
  // Save the original module base. (Returned by GWT.getModuleBaseURL.)
  $wnd[devModeHookKey + ':moduleBase'] = computeScriptBase();

  // Needed in the real nocache.js logic
  $wnd.__gwt_activeModules['crimemapper'].canRedirect = true;
  $wnd.__gwt_activeModules['crimemapper'].superdevmode = true;

  // Insert the superdevmode nocache script in the first position of the head
  var devModeScript = $doc.createElement('script');
  devModeScript.src = nocacheUrl;

  // Show a div in a corner for adding buttons to recompile the app.
  // We reuse the same div in all modules of this page for stacking buttons
  // and to make it available in jsni.
  // The user can remove this: .gwt-DevModeRefresh {display:none}
  $wnd.__gwt_compileElem = $wnd.__gwt_compileElem || $doc.createElement('div');
  $wnd.__gwt_compileElem.className = 'gwt-DevModeRefresh';

  // Create the compile button for this module
  var compileButton = $doc.createElement('div');
  $wnd.__gwt_compileElem.appendChild(compileButton);
  // Number of modules present in the window
  var moduleIdx = $wnd.__gwt_compileElem.childNodes.length;
  // Each button has a class with its index number
  var buttonClassName = 'gwt-DevModeCompile gwt-DevModeModule-' + moduleIdx;
  compileButton.className = buttonClassName;
  // The status message container
  compileButton.innerHTML = '<div></div>';
  // User knows who module to compile, hovering the button
  compileButton.title = 'Compile module:\ncrimemapper';

  // Use CSS so the app could change button style
  var compileStyle = $doc.createElement('style');
  compileStyle.language = 'text/css';
  $head.appendChild(compileStyle);
  var css =
    ".gwt-DevModeRefresh{" +
      "position:fixed;" +
      "right:3px;" +
      "bottom:3px;" +
      "font-family:arial;" +
      "font-size:1.8em;" +
      "cursor:pointer;" +
      "color:#B62323;" +
      "text-shadow:grey 1px 1px 3px;" +
      "z-index:2147483646;" +
      "white-space:nowrap;" +
    "}" +
    ".gwt-DevModeCompile{" +
      "position:relative;" +
      "float:left;" +
      "width:1em;" +
    "}" +
    ".gwt-DevModeCompile div{" +
      "position:absolute;" +
      "right:1em;" +
      "bottom:-3px;" +
      "font-size:0.3em;" +
      "opacity:1;" +
      "direction:rtl;" +
    "}" +
    ".gwt-DevModeCompile:before{" +
      "content:'\u21bb';" +
    "}" +
    ".gwt-DevModeCompiling:before{" +
      // IE8 fails when setting content here
      "opacity:0.1;" +
    "}" +
    ".gwt-DevModeCompile div:before{" +
      "content:'GWT';" +
    "}" +
    ".gwt-DevModeError div:before{" +
      "content:'FAILED';" +
    "}";
  // Only insert common css the first time
  css = (moduleIdx == 1 ? css : '') +
    ".gwt-DevModeModule-" + moduleIdx + ".gwt-DevModeCompiling div:before{" +
      "content:'COMPILING crimemapper';" +
      "font-size:24px;" +
      "color:#d2d9ee;" +
    "}";
  if ('styleSheet' in compileStyle) {
    // IE8
    compileStyle.styleSheet.cssText = css;
  } else {
    compileStyle.appendChild($doc.createTextNode(css));
  }

  // Set a different compile function name per module
  var compileFunction = '__gwt_compile_' + moduleIdx;

  compileButton.onclick = function() {
    $wnd[compileFunction]();
  };

  // defer so as the body is ready
  setTimeout(function(){
    $head.insertBefore(devModeScript, $head.firstElementChild || $head.children[0]);
    $doc.body.appendChild($wnd.__gwt_compileElem);
  }, 1);

  // Flag to avoid compiling in parallel.
  var compiling = false;
  // Compile function available in window so as it can be run from jsni.
  // TODO(manolo): make Super Dev Mode script set this function in __gwt_activeModules
  $wnd[compileFunction] = function() {
    if (compiling) {
      return;
    }
    compiling = true;

    // Compute an unique name for each callback to avoid cache issues
    // in IE, and to avoid the same function being called twice.
    var callback = '__gwt_compile_callback_' + moduleIdx + '_' + new Date().getTime();
    $wnd[callback] = function(r) {
      if (r && r.status && r.status == 'ok') {
        $wnd.location.reload();
      }
      compileButton.className = buttonClassName + ' gwt-DevModeError';
      delete $wnd[callback];
      compiling = false;
    };

    // Insert the jsonp script to compile the current module
    // TODO(manolo): we don't have a way to detect when the server is unreachable,
    // maybe a request returning status='idle'
    var compileScript = $doc.createElement('script');
    compileScript.src = serverUrl +
      '/recompile/crimemapper?user.agent=' + ua + '&_callback=' + callback;
    $head.appendChild(compileScript);
    compileButton.className = buttonClassName  + ' gwt-DevModeCompiling';
  }

  // Run this block after the app has been loaded.
  setTimeout(function(){
    // Maintaining the hook key in session can cause problems
    // if we try to run classic code server so we remove it
    // after a while.
    $wnd.sessionStorage.removeItem(devModeHookKey);

    // Re-attach compile button because sometimes app clears the dom
    $doc.body.appendChild($wnd.__gwt_compileElem);
  }, 2000);
})(window, document);
=======
function crimemapper(){var bb='',$=' top: -1000px;',yb='" for "gwt:onLoadErrorFn"',wb='" for "gwt:onPropertyErrorFn"',hb='");',zb='#',$b='.cache.js',Bb='/',Hb='//',Ub='03FC960186AC6534424249BA7871B2C7',Vb='53E3F80918B629E44A63029476C92F1D',Zb=':',qb='::',kc=':moduleBase',ab='<!doctype html>',cb='<html><head><\/head><body><\/body><\/html>',tb='=',Ab='?',Wb='A144B050FA5A369A0C8FAF7027753F1F',Xb='BC78A02844553043595AE927F82C735E',vb='Bad handler "',_='CSS1Compat',fb='Chrome',eb='DOMContentLoaded',V='DUMMY',Yb='E594ABCBCCCD0CC9F086F05AA2F6D42D',jc='Ignoring non-whitelisted Dev Mode URL: ',ic='__gwtDevModeHook:crimemapper',Gb='base',Eb='baseUrl',Q='begin',W='body',P='bootstrap',Db='clear.cache.gif',sb='content',T='crimemapper',Tb='crimemapper.devmode.js',Fb='crimemapper.nocache.js',pb='crimemapper::',fc='end',gb='eval("',hc='file:',Pb='gecko',Qb='gecko1_8',R='gwt.codesvr.crimemapper=',S='gwt.codesvr=',ec='gwt/clean/clean.css',xb='gwt:onLoadErrorFn',ub='gwt:onPropertyErrorFn',rb='gwt:property',mb='head',cc='href',gc='http:',Mb='ie10',Ob='ie8',Nb='ie9',X='iframe',Cb='img',jb='javascript',Y='javascript:""',_b='link',dc='loadExternalRefs',nb='meta',lb='moduleRequested',kb='moduleStartup',Lb='msie',ob='name',Z='position:absolute; width:0; height:0; border:none; left: -1000px;',ac='rel',Kb='safari',ib='script',Sb='selectingPermutation',U='startup',bc='stylesheet',db='undefined',Rb='unknown',Ib='user.agent',Jb='webkit';var o=window;var p=document;r(P,Q);function q(){var a=o.location.search;return a.indexOf(R)!=-1||a.indexOf(S)!=-1}
function r(a,b){if(o.__gwtStatsEvent){o.__gwtStatsEvent({moduleName:T,sessionId:o.__gwtStatsSessionId,subSystem:U,evtGroup:a,millis:(new Date).getTime(),type:b})}}
crimemapper.__sendStats=r;crimemapper.__moduleName=T;crimemapper.__errFn=null;crimemapper.__moduleBase=V;crimemapper.__softPermutationId=0;crimemapper.__computePropValue=null;crimemapper.__getPropMap=null;crimemapper.__gwtInstallCode=function(){};crimemapper.__gwtStartLoadingFragment=function(){return null};var s=function(){return false};var t=function(){return null};__propertyErrorFunction=null;var u=o.__gwt_activeModules=o.__gwt_activeModules||{};u[T]={moduleName:T};var v;function w(){B();return v}
function A(){B();return v.getElementsByTagName(W)[0]}
function B(){if(v){return}var a=p.createElement(X);a.src=Y;a.id=T;a.style.cssText=Z+$;a.tabIndex=-1;p.body.appendChild(a);v=a.contentDocument;if(!v){v=a.contentWindow.document}v.open();var b=document.compatMode==_?ab:bb;v.write(b+cb);v.close()}
function C(k){function l(a){function b(){if(typeof p.readyState==db){return typeof p.body!=db&&p.body!=null}return /loaded|complete/.test(p.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(p.removeEventListener){p.removeEventListener(eb,d,false)}if(e){clearInterval(e)}}}
if(p.addEventListener){p.addEventListener(eb,d,false)}var e=setInterval(function(){if(b()){d()}},50)}
function m(c){function d(a,b){a.removeChild(b)}
var e=A();var f=w();var g;if(navigator.userAgent.indexOf(fb)>-1&&window.JSON){var h=f.createDocumentFragment();h.appendChild(f.createTextNode(gb));for(var i=0;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(f.createTextNode(j.substring(1,j.length-1)))}h.appendChild(f.createTextNode(hb));g=f.createElement(ib);g.language=jb;g.appendChild(h);e.appendChild(g);d(e,g)}else{for(var i=0;i<c.length;i++){g=f.createElement(ib);g.language=jb;g.text=c[i];e.appendChild(g);d(e,g)}}}
crimemapper.onScriptDownloaded=function(a){l(function(){m(a)})};r(kb,lb);var n=p.createElement(ib);n.src=k;p.getElementsByTagName(mb)[0].appendChild(n)}
crimemapper.__startLoadingFragment=function(a){return G(a)};crimemapper.__installRunAsyncCode=function(a){var b=A();var c=w().createElement(ib);c.language=jb;c.text=a;b.appendChild(c);b.removeChild(c)};function D(){var c={};var d;var e;var f=p.getElementsByTagName(nb);for(var g=0,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(ob),k;if(j){j=j.replace(pb,bb);if(j.indexOf(qb)>=0){continue}if(j==rb){k=i.getAttribute(sb);if(k){var l,m=k.indexOf(tb);if(m>=0){j=k.substring(0,m);l=k.substring(m+1)}else{j=k;l=bb}c[j]=l}}else if(j==ub){k=i.getAttribute(sb);if(k){try{d=eval(k)}catch(a){alert(vb+k+wb)}}}else if(j==xb){k=i.getAttribute(sb);if(k){try{e=eval(k)}catch(a){alert(vb+k+yb)}}}}}t=function(a){var b=c[a];return b==null?null:b};__propertyErrorFunction=d;crimemapper.__errFn=e}
function F(){function e(a){var b=a.lastIndexOf(zb);if(b==-1){b=a.length}var c=a.indexOf(Ab);if(c==-1){c=a.length}var d=a.lastIndexOf(Bb,Math.min(c,b));return d>=0?a.substring(0,d+1):bb}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=p.createElement(Cb);b.src=a+Db;a=e(b.src)}return a}
function g(){var a=t(Eb);if(a!=null){return a}return bb}
function h(){var a=p.getElementsByTagName(ib);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(Fb)!=-1){return e(a[b].src)}}return bb}
function i(){var a=p.getElementsByTagName(Gb);if(a.length>0){return a[a.length-1].href}return bb}
function j(){var a=p.location;return a.href==a.protocol+Hb+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==bb){k=h()}if(k==bb){k=i()}if(k==bb&&j()){k=e(p.location.href)}k=f(k);return k}
function G(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return crimemapper.__moduleBase+a}
function H(){var f=[];var g;function h(a,b){var c=f;for(var d=0,e=a.length-1;d<e;++d){c=c[a[d]]||(c[a[d]]=[])}c[a[e]]=b}
var i=[];var j=[];function k(a){var b=j[a](),c=i[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(__propertyErrorFunc){__propertyErrorFunc(a,d,b)}throw null}
j[Ib]=function(){var b=navigator.userAgent.toLowerCase();var c=function(a){return parseInt(a[1])*1000+parseInt(a[2])};if(function(){return b.indexOf(Jb)!=-1}())return Kb;if(function(){return b.indexOf(Lb)!=-1&&p.documentMode>=10}())return Mb;if(function(){return b.indexOf(Lb)!=-1&&p.documentMode>=9}())return Nb;if(function(){return b.indexOf(Lb)!=-1&&p.documentMode>=8}())return Ob;if(function(){return b.indexOf(Pb)!=-1}())return Qb;return Rb};i[Ib]={gecko1_8:0,ie10:1,ie8:2,ie9:3,safari:4};s=function(a,b){return b in i[a]};crimemapper.__getPropMap=function(){var a={};for(var b in i){if(i.hasOwnProperty(b)){a[b]=k(b)}}return a};crimemapper.__computePropValue=k;o.__gwt_activeModules[T].bindings=crimemapper.__getPropMap;r(P,Sb);if(q()){return G(Tb)}var l;try{h([Nb],Ub);h([Qb],Vb);h([Kb],Wb);h([Ob],Xb);h([Mb],Yb);l=f[k(Ib)];var m=l.indexOf(Zb);if(m!=-1){g=parseInt(l.substring(m+1),10);l=l.substring(0,m)}}catch(a){}crimemapper.__softPermutationId=g;return G(l+$b)}
function I(){if(!o.__gwt_stylesLoaded){o.__gwt_stylesLoaded={}}function c(a){if(!__gwt_stylesLoaded[a]){var b=p.createElement(_b);b.setAttribute(ac,bc);b.setAttribute(cc,G(a));p.getElementsByTagName(mb)[0].appendChild(b);__gwt_stylesLoaded[a]=true}}
r(dc,Q);c(ec);r(dc,fc)}
D();crimemapper.__moduleBase=F();u[T].moduleBase=crimemapper.__moduleBase;var J=H();if(o){var K=!!(o.location.protocol==gc||o.location.protocol==hc);o.__gwt_activeModules[T].canRedirect=K;if(K){var L=ic;var M=o.sessionStorage[L];if(!/^http:\/\/(localhost|127\.0\.0\.1)(:\d+)?\/.*$/.test(M)){if(M&&(window.console&&console.log)){console.log(jc+M)}M=bb}if(M&&!o[L]){o[L]=true;o[L+kc]=F();var N=p.createElement(ib);N.src=M;var O=p.getElementsByTagName(mb)[0];O.insertBefore(N,O.firstElementChild||O.children[0]);return false}}}I();r(P,fc);C(J);return true}
crimemapper.succeeded=crimemapper();
>>>>>>> 71fbcad2334c27375ad55c00c3ad36f3ac09a17e
