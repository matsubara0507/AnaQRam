# AnaQRam

- QRコードを利用したアナグラム(並び替えパズル)
  - アナグラムの文字を最初は伏字にしている
  - QRコードをスキャンすることで文字が見えるようになる
  - 全て見える状態で並び替えるて答えの文字列を作る
- IGGGのイベントのために作る

## 機能

- QRコードは数字(0,1,2...)
  - 数字に対応する文字が見えるようになる
- 文字列長以上の数字を読み取っても問題ない
  - 剰余を取ってるため
- 問題の文字列は設定でいじれる

## 参照

- QRコードの読み取り
  - https://github.com/tutsplus/Android-MobileVisionAPI
- 設定画面の追加
  - http://blogand.stack3.net/archives/239
  - http://appdevmem.blogspot.jp/2015/09/android-app-settings.html
