# app/db/base.py
from sqlalchemy.orm import DeclarativeBase


class Base(DeclarativeBase):
    pass


#  아래에 모델들을 import 해서 "모델 등록"을 발생시켜야
#  Base.metadata에 테이블이 제대로 모입니다.
#  (안 하면 create_all 해도 테이블이 안 생기는 흔한 사고가 납니다.)
