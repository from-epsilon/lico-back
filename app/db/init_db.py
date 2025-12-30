# app/db/init_db.py
from sqlalchemy.ext.asyncio import AsyncEngine
from app.db.base import Base  # Base.metadata에 모델들이 import되어 있어야 함


async def init_db(engine: AsyncEngine) -> None:
    """
    DB 초기화: 테이블 생성.
    - MVP/개발 단계용(create_all)
    - 운영에서는 Alembic migration 권장
    """
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
