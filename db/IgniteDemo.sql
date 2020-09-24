IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'IgniteDemo')
    BEGIN
        CREATE DATABASE [IgniteDemo]
    END
GO
    USE [IgniteDemo]
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Instruments' and xtype='U')
    BEGIN
        CREATE TABLE [dbo].[Instruments]
        (
            [id]							BIGINT        NOT NULL IDENTITY,
            [name]						VARCHAR(100)  NOT NULL,
            [market]					    VARCHAR(100)  NOT NULL,

            CONSTRAINT PK_INSTRUMENTS PRIMARY KEY CLUSTERED (id)
        )
    END

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Users' and xtype='U')
    BEGIN
        CREATE TABLE [dbo].[Users]
        (
            [id]							BIGINT        NOT NULL IDENTITY,
            [first_name]					VARCHAR(100)  NOT NULL,
            [last_name]					VARCHAR(100)  NOT NULL,

            CONSTRAINT PK_USERS PRIMARY KEY CLUSTERED (id)
        )
    END

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Accounts' and xtype='U')
    BEGIN
        CREATE TABLE [dbo].[Accounts]
        (
            [id]							BIGINT        NOT NULL IDENTITY,
            [name]						VARCHAR(100)  NOT NULL,
            [user_id]						BIGINT		  NOT NULL,

            CONSTRAINT PK_ACCOUNTS PRIMARY KEY CLUSTERED ([id]),
            CONSTRAINT AK_NAME UNIQUE ([name]),
            CONSTRAINT FK_ACCOUNTS_USER_ID FOREIGN KEY ([user_id]) REFERENCES [dbo].[Users]([id]),
        )
    END

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Orders' and xtype='U')
    BEGIN
        CREATE TABLE [dbo].[Orders]
        (
            [id]								BIGINT  NOT NULL IDENTITY,
            [volume]							BIGINT  NOT NULL,
            [price]							DECIMAL NOT NULL,
            [account_id]						BIGINT  NOT NULL,
            [instrument_id]					BIGINT  NOT NULL,

            CONSTRAINT PK_ORDERS PRIMARY KEY CLUSTERED ([id]),
            CONSTRAINT FK_ORDERS_ACCOUNT_ID FOREIGN KEY ([account_id]) REFERENCES [dbo].[Accounts]([id]),
            CONSTRAINT FK_ORDERS_INSTRUMENT_ID FOREIGN KEY ([instrument_id]) REFERENCES [dbo].[Instruments]([id]),
        )
    END





