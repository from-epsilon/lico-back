from fastapi import APIRouter
from app.api.v1.endpoints import health, auth

api_router = APIRouter()
api_router.include_router(health.router, prefix="/api/v1", tags=["health"])
api_router.include_router(auth.router, prefix="/api/v1/auth", tags=["auth"])
