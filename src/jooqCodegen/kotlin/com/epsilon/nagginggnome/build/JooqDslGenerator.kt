package com.epsilon.nagginggnome.build

import org.flywaydb.core.Flyway
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object JooqDslGenerator {

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDir: String = args.getOrNull(
            index = 0
        )
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("An output directory path (args[0]) is required.")
        val imageName: DockerImageName = DockerImageName.parse("postgres:18-alpine")
        PostgreSQLContainer<Nothing>(imageName).apply {
            withDatabaseName("codegen_db")
            withUsername("codegen_user")
            withPassword("codegen_pw")
        }.use { postgres ->
            postgres.start()
            val jdbcUrl: String = postgres.jdbcUrl
            val username: String = postgres.username
            val password: String = postgres.password
            Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .locations("filesystem:src/main/resources/db/migration")
                .load()
                .migrate()
            val configuration: Configuration = Configuration()
                .withJdbc(
                    Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(jdbcUrl)
                        .withUser(username)
                        .withPassword(password)
                )
                .withGenerator(
                    Generator()
                        .withName("org.jooq.codegen.KotlinGenerator")
                        .withDatabase(
                            Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withInputSchema("public")
                                .withIncludes("(push_.*|llm_jobs|user_summaries|users|server_messages|user_messages|fcm_tokens|user_settings)")
                                .withExcludes("flyway_schema_history")
                                .withForcedTypes(
                                    listOf(
                                        ForcedType()
                                            .withUserType("java.time.Instant")
                                            .withConverter(
                                                "com.epsilon.nagginggnome.infra.persistence.jooq.converter.OffsetDateTimeToInstantConverter"
                                            )
                                            .withIncludeTypes("(?i:timestamp with time zone|timestamptz)")
                                    )
                                )
                        ).withGenerate(
                            Generate()
                                .withKotlinNotNullRecordAttributes(true)
                                .withKotlinNotNullPojoAttributes(true)
                                .withKotlinNotNullInterfaceAttributes(true)
                        )
                        .withTarget(
                            Target()
                                .withPackageName("com.epsilon.nagginggnome.generated.jooq")
                                .withDirectory(outputDir)
                        )
                )
            GenerationTool.generate(configuration)
        }
    }
}
