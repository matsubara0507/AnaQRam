# AnaQRam

- QRコードを利用したアナグラム(並び替えパズル)
  - アナグラムの文字を最初は伏字にしている
  - QRコードをスキャンすることで文字が見えるようになる
  - 全て見える状態で並び替えるて答えの文字列を作る
- IGGGのイベントのために作る

## 遊び方

QRコードをスキャンして
![](/img/anaqram1.gif)

文字を並び替えて単語を作る(アナグラム)
![](/img/anaqram2.gif)

QRコードは `img/qrcodes` に置いてある．

### 依存するアプリなど

- [Google Play開発者サービス](https://play.google.com/store/apps/details?id=com.google.android.gms&hl=ja): QRコードをこれでスキャン
- [Google 日本語入力](https://play.google.com/store/apps/details?id=com.google.android.inputmethod.japanese&hl=ja): 日本語の答えを入力するのに必要
- WiFi: インターネットに接続されてないとスコアのポストができない

## 機能

- QRコードは数字(0,1,2...)
  - 数字に対応する文字が見えるようになる
- 文字列長以上の数字を読み取っても問題ない
  - 剰余を取ってるため
- 問題の文字列は設定でいじれる
- 画面を回転してもリセットされないようにした
  - ただ `android:configChanges="orientation|screenSize"` を加えただけ
  - TODO: もっと良い方法はあるのだろうか
- スタート時に文字列がシャッフルされるようにした
  - 答えを変えてもされるはず
- 文字列が合っていたら「くりあ～！」と出る
- タイマー機能を追加
  - スタートボタンを押さないとスキャンできない
  - スタートボタンを押すとタイマースタート
  - スタートするとリセットボタンに変わる
  - リセットボタンを押すと全てリセットされる

#### Version 2

- スコアランキングのサーバーにクリア後スコアをポストする
    - 答えの文字数・クリアタイム・入れ替え回数
- ボタン配置を横スクロールするようにした
    - 実質的に文字制限が無くなった
- BUGFIX: QRコード見つけたときに文字列が合っていてもクリアにならない
- BUGFIX: クリア後に設定を変えるとクリアタイムあたりがおかしい

## 参照

- QRコードの読み取り
  - https://github.com/tutsplus/Android-MobileVisionAPI
- 設定画面の追加
  - http://blogand.stack3.net/archives/239
  - http://appdevmem.blogspot.jp/2015/09/android-app-settings.html
- 画面回転時のリセット
  - http://qiita.com/sy_sft_/items/502280b5e41b2ff0dfa9
  - これはだめだった
	- https://www.ipentec.com/document/document.aspx?page=android-rotate-screen-save-value-in-bundle
- シャッフル
  - http://javatechnology.net/java/list-shuffle/
- トースト
  - http://blog.fujiu.jp/2013/11/14-android-toast.html
- タイマー
  - https://akira-watson.com/android/timertask.html
- 横スクロール
  - https://akira-watson.com/android/horizontal-scrollview.html
  - 中央にしたい: https://stackoverflow.com/questions/5478502/horizontalscrollview-with-centered-elements
